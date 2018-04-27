from PIL import Image
import math

print("Input the image's name and its file extension")
print("Matrix will be saved at imageMatrix.txt")
print("W H data[i][j] ...")

fil = open('textImage.txt', 'w')

name = input()

img = Image.open(name).convert('L')
WIDTH, HEIGHT = img.size

data = list(img.getdata())
data = [data[offset:offset+WIDTH] for offset in range(0, WIDTH*HEIGHT, WIDTH)]

# At this point the image's pixels are all in memory and can be accessed
# individually using data[row][col].

fil.write(str(WIDTH))
fil.write(' ')
fil.write(str(HEIGHT))
fil.write(' ')

for i in range(WIDTH):
    for j in range(HEIGHT):
        fil.write(str(data[j][i]))
        fil.write(' ')
