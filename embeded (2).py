from time import sleep
import RPi.GPIO as GPIO
import socket

GPIO.setmode(GPIO.BOARD)
rlinpin=8
hallpin=12
GPIO.setup(rlinpin, GPIO.OUT, initial=GPIO.LOW)
GPIO.setup(hallpin, GPIO.IN)
TCP_IP = '192.168.0.17'
TCP_PORT = 1252
CODE_CHECK_DOOR = b'\x01'
CODE_OPEN_DOOR = b'\x02'

def door_openner(command):
    if (command):
        GPIO.output(rlinpin, GPIO.HIGH)
        print("deblocat")
        sleep(5)
        GPIO.output(rlinpin, GPIO.LOW)
        print("blocat")
    else:
        GPIO.output(rlinpin, GPIO.LOW)
        print("blocat")

def door_status_response(response):
    if(GPIO.input(hallpin)):
        response[0] = 1;
    else:
        response[0] = 2;

def door_status():
    doorStatus = [1]
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((TCP_IP, TCP_PORT))
    server_socket.listen(100)

    while True:
        connection, address = server_socket.accept()

        code = connection.recv(1)
        print(code)

        if code == CODE_CHECK_DOOR:
            door_status_response(doorStatus)
            connection.send(bytearray([doorStatus[0]]))
        elif code == CODE_OPEN_DOOR:
            open_door(True)

        connection.close()

door_status()
