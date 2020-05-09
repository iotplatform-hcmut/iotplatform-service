import paho.mqtt.client as mqtt
import time

# IP address and Port
HOST, PORT = "iotplatform.xyz", 1883
# Topic name
topic = "test"
# Date send
data = '{"value": 12}'

client = mqtt.Client()
client.connect(HOST, PORT, 60)

while(True):
    time.sleep(1)
    client.publish(topic , data)

client.disconnect()