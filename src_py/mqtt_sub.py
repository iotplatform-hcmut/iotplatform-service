import json
import time

import paho.mqtt.client as mqtt
import pymysql.cursors
from pymysqlpool.pool import Pool

# MQTT Configuration
MQTT_HOST = '13.76.250.158'
MQTT_PORT = 1883
MQTT_USERNAME = 'BKvm2'
MQTT_PASSWORD = 'Hcmut_CSE_2020'
PUB_TOPIC = ''
SUB_TOPIC = 'Topic/Mois'

# Database Configuration
DB_HOST = 'iotplatform.xyz'
DB_PORT = 3306
DB_USERNAME = 'root'
DB_PASSWORD = 'a123'
DB_SCHEMA = 'iotplatform'
SQL = 'INSERT INTO `humidity` (`id`, `timestamp`, `value`) VALUES (%s, %s, %s)'


def extract_data(data: list) -> [[str, int]]:
    result = []
    for json in data:
        id = json['device_id']
        pair = json['values']
        value = int(pair[0])
        result += [[id, value]]
    return result


def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print('[INFO] CONNECT MQTT BROKER: SUCCESS')
        global Connected
        Connected = True
    else:
        print('[ERROR] CONNECT MQTT BROKER: FAIL')

    print('[INFO] %s %s' % (MQTT_HOST, MQTT_PORT))


def on_message(client, userdata, message: mqtt.MQTTMessage):
    data = json.loads(message.payload.decode("utf-8"))
    data_list = None
    try:
        data_list = extract_data(data)
    except:
        print('[ERROR] JSON FORMAT IS WRONG.')
        return

    connection = pool.get_conn()
    try:
        with connection.cursor() as cursor:
            # Create a new record
            timestamp = int(time.time())
            for data in data_list:
                cursor.execute(SQL, (data[0], timestamp, data[1]))
                print('[INFO] INSERT DATABASE: SUCCESS')
                print('[INFO] %s, %s, %s' %
                      (data[0], data[1], time.ctime(timestamp)))
    except:
        print('[ERROR] INSERT DATABASE: FAIL')
    finally:
        pool.release(connection)


if __name__ == '__main__':

    pool = Pool(host=DB_HOST, port=DB_PORT, user=DB_USERNAME, password=DB_PASSWORD,
                db=DB_SCHEMA, cursorclass=pymysql.cursors.DictCursor,
                autocommit=True, min_size=1, max_size=10)
    pool.init()

    Connected = False

    client = mqtt.Client()
    client.on_connect = on_connect
    client.on_message = on_message
    client.username_pw_set(MQTT_USERNAME, MQTT_PASSWORD)

    client.connect(MQTT_HOST)
    client.loop_start()

    while Connected != True:
        time.sleep(0.1)

    client.subscribe(SUB_TOPIC)

    try:
        while True:
            time.sleep(1)

    except (KeyboardInterrupt, SystemExit):
        client.disconnect()
        client.loop_stop()
        pool.destroy()
