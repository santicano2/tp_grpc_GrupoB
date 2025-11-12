FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .
COPY ../kafka-server/kafka_manager.py /app/kafka_manager.py

EXPOSE 8090

CMD ["python", "kafka_server.py"]