import socket
import face_recognition
from time import sleep

TCP_IP = '192.168.0.17'
TCP_PORT = 1251
DOOR_STATUS = 1


def face_recognition_fun():
    picture_of_me = face_recognition.load_image_file("A.jpg")
    my_face_encoding = face_recognition.face_encodings(picture_of_me)[0]

    # my_face_encoding now contains a universal 'encoding' of my facial features that can be compared to any other picture of a face!

    unknown_picture = face_recognition.load_image_file("a1.jpg")
    unknown_face_encoding = face_recognition.face_encodings(unknown_picture)[0]

    # Now we can see the two face encodings are of the same person with `compare_faces`!

    results = face_recognition.compare_faces([my_face_encoding], unknown_face_encoding)

    return results[0]

def receive_image(filename):
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((TCP_IP, TCP_PORT))
    server_socket.listen(100)

    while True:
        connection, address = server_socket.accept()

        try:
            file = open(filename, "wb")
            current_buffer_size = connection.recv(4)
            current_buffer_size = int.from_bytes(current_buffer_size, byteorder='big')

            while current_buffer_size > 0:
                print(current_buffer_size)

                buffer = bytearray(connection.recv(current_buffer_size))
                print(len(buffer))
                file.write(buffer)
                
                current_buffer_size = connection.recv(4)
                current_buffer_size = int.from_bytes(current_buffer_size, byteorder='big')
            file.close()
            if face_recognition_fun():
                print("true")
                connection.send(bytearray([0, 0, 0, DOOR_STATUS]))
            else :
                connection.send(bytearray([255, 255, 255, 255])
                print("false")
            connection.close()
        except:
            pass


receive_image("//home//pi//Desktop//a1.jpg")
