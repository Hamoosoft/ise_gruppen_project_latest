#!/bin/bash

# Default values
HOST="localhost"
PORT="30001"
JMETER_PATH="jmeter" # Assumes jmeter is in PATH, otherwise set this to /path/to/jmeter

usage() {
    echo "Usage: $0 [OPTIONS]"
    echo "Options:"
    echo "  -h, --host HOST      Target host (default: localhost)"
    echo "  -p, --port PORT      Target port (default: 30001)"
    echo "  -j, --jmeter PATH    Path to JMeter executable (default: jmeter)"
    echo "  --help               Show this help message"
    exit 1
}

# Parse arguments
while [[ "$#" -gt 0 ]]; do
    case $1 in
        -h|--host) HOST="$2"; shift ;;
        -p|--port) PORT="$2"; shift ;;
        -j|--jmeter) JMETER_PATH="$2"; shift ;;
        --help) usage ;;
        *) echo "Unknown parameter passed: $1"; usage ;;
    esac
    shift
done

echo "Starting Load Test on $HOST:$PORT using $JMETER_PATH..."

# Check if JMeter is installed/found
if ! command -v "$JMETER_PATH" &> /dev/null; then
    echo "Error: JMeter not found at '$JMETER_PATH'. Please install it or provide path with -j."
    exit 1
fi

# Run JMeter in non-GUI mode
"$JMETER_PATH" -n \
    -t tests/load_test_plan.jmx \
    -l results.jtl \
    -JHOST="$HOST" \
    -JPORT="$PORT"

echo "Load Test Finished. Results saved to results.jtl"
