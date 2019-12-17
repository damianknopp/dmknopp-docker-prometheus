FROM golang:alpine AS builder
RUN apk update && apk add --no-cache git
WORKDIR $GOPATH/src/random-scraper
COPY main.go .
RUN go get -d -v
RUN CGO_ENABLED=0 go build -o /go/bin/random
FROM scratch AS final
COPY --from=builder /go/bin/random /go/bin/random
ENTRYPOINT ["/go/bin/random", "-listen-address=:8080"]