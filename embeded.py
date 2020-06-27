from time import sleep
import RPi.GPIO as GPIO
import socket
GPIO.setmode(GPIO.BOARD)
rlinpin=8
hallpin=12
command= False
GPIO.setup(rlinpin, GPIO.OUT, initial=GPIO.LOW)
GPIO.setup(hallpin, GPIO.IN)

while True:
    if (command == 1):
        GPIO.output(rlinpin, GPIO.HIGH)
        print("deblocat")
        sleep(5)
        GPIO.output(rlinpin, GPIO.LOW)
        print("blocat")
    else:
        GPIO.output(rlinpin, GPIO.LOW)
        print("blocat")
    
    if(GPIO.input(hallpin)== True):
        print("Usa deschisa")
        stare = False
        sleep(3)
        
    else:
        print("usa inschisa")
        stare = True
    
    command = not command
