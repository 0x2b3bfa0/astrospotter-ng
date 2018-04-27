from PIL import Image
import random
import math

PATH = open('PATH.txt', 'r').read()

inp = open(PATH + '\\fil\\names.txt').read().split(',')
fil = open(PATH + '\\fil\\matrices.txt', 'w')

rand_max = 150

n = int(inp[0])

fil.write(str(n))
fil.write(' ')

for i in range(n):
    name = inp[i+1]
    
    img = Image.open(PATH + '\\cas\\actual\\' + name).convert('L')
    WIDTH, HEIGHT = img.size

    data = list(img.getdata())
    data = [data[offset:offset+WIDTH] for offset in range(0, WIDTH*HEIGHT, WIDTH)]

    # At this point the image's pixels are all in memory and can be accessed
    # individually using data[row][col].

    if i == 0:
        fil.write(str(WIDTH))
        fil.write(' ')
        fil.write(str(HEIGHT))
        fil.write(' ')

    for i in range(WIDTH):
        for j in range(HEIGHT):
            fil.write( str(min(data[j][i] + random.randint(0, rand_max), 255)) )
            fil.write(' ')

