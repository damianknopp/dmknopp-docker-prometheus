package dmk.prometheus.scraper;

import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.DoubleSupplier;

import static java.lang.Math.sin;

public class App extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    private final double uniformDomain = 0.0002;
    private final double normDomain = 0.0002;
    private final double normMean = 0.00001;
    private Duration oscillationPeriod = Duration.ofMinutes(10);
    private Summary rpcDurations;
    private Histogram rpcDurationsHistogram;

    public App() {
        super();
        this.init();
    }

    public void init() {
        logger.debug("initializing all sample generators");
        // Create a summary to track fictional interservice RPC latencies for three
        // distinct services with different latency distributions. These services are
        // differentiated via a "service" label.
        this.rpcDurations = Summary.build()
                .name("rpc_durations_seconds")
                .help("RPC latency distributions.")
                .quantile(.5, .05)
                .quantile(.9, .01)
                .quantile(.99, .001)
                .labelNames("service")
                .create();

        // The same as above, but now as a histogram, and only for the normal
        // distribution. The buckets are targeted to the parameters of the
        // normal distribution, with 20 buckets centered on the mean, each
        // half-sigma wide.
        this.rpcDurationsHistogram = Histogram.build()
                .name("rpc_durations_histogram_seconds")
                .help("RPC latency distributions.")
                .linearBuckets((normMean - (5 * normDomain)), .5 * normDomain, 20)
                .create();

        rpcDurations.register();
        rpcDurationsHistogram.register();
    }

    public void runAll() {
        logger.debug("starting all sample generators");
        LocalDateTime start = LocalDateTime.now();
        DoubleSupplier oscillationFactor = () -> {
            Duration duration = Duration.between(start, LocalDateTime.now());
            return 2 + sin(sin(2 * Math.PI * duration.toMillis()) / oscillationPeriod.toMillis());
        };

        // Periodically record some sample latencies for the three services.
        Runnable recordSampleLatency1 = () -> {
            do {
                double v = Math.random() * uniformDomain;
                rpcDurations.labels("uniform").observe(v);
                try {
                    Thread.sleep(((long) (100 * oscillationFactor.getAsDouble())));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        };

        Runnable recordSampleLatency2 = () -> {
            do {
                double v = (Math.random() * normDomain) + normMean;
                rpcDurations.labels("normal").observe(v);
                rpcDurationsHistogram.observe(v);
                try {
                    Thread.sleep(((long) (75 * oscillationFactor.getAsDouble())));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        };

        Runnable recordSampleLatency3 = () -> {
            do {
                double v = Math.random() / 1e6;
                rpcDurations.labels("exponential").observe(v);
                rpcDurationsHistogram.observe(v);
                try {
                    Thread.sleep(((long) (50 * oscillationFactor.getAsDouble())));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        };

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(recordSampleLatency1);
        executorService.submit(recordSampleLatency2);
        executorService.submit(recordSampleLatency3);
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
//        executor.scheduleAtFixedRate(recordSampleLatencies, 0, 100 * oscillationFactor.apply(), TimeUnit.MILLISECONDS);
    }

    public void start() {
        this.runAll();
        JsonObject jsonObject = context.config();
        String json = context.config().encodePrettily();
        logger.debug(json);
        Integer port = jsonObject.getJsonObject("http").getInteger("port");

        Router router = Router.router(vertx);
        router.route("/metrics").handler((ctx) -> {
            HttpServerResponse response = ctx.response();
            response.putHeader("content-type", "text/plain");
            // Write to the response and end it
            response.end("Hello World from Vert.x-Web!");
        });
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(router).listen(port);
    }
}