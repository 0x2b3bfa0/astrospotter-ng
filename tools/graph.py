#!/usr/bin/env python3.6
"""Represent visually the luminosity.txt and center.txt datasets."""

import click
import numpy
import matplotlib.pyplot as plt
import matplotlib as mpl

with open("./luminosity.txt", 'r') as luminosity:
    luminosity = luminosity.read().split()
with open("./center.txt", 'r') as centers:
    centers = centers.read().split()

objects, frames, width, height, *data = map(float, centers)

o = int(objects)
n = int(frames)
W = int(width)
H = int(height)

plt.figure(figsize=(7, 7))
plt.title('Moving Centers')
plt.grid(True)

yy = numpy.arange(0, H, 20)
xx = numpy.arange(0, W, 20)

plt.ylabel('H')
plt.yticks(yy)
plt.ylim([0, H])

plt.xlabel('W')
plt.xticks(xx)
plt.xlim([0, W])
it = 0

for i in range(o):
    data1 = []
    data2 = []
    for j in range(n):
        data1.append(float(data[it]))
        it = it+1
        data2.append(H - float(data[it]))
        it = it+1
    plt.plot(data1, data2, 'o', label=('Object '+str(i+1)))
plt.legend()
# plt.show()
plt.savefig("./center.png")

object, frames, *data = luminosity
o = int(object)
n = int(frames)
it = 0

xx = numpy.arange(n)

plt.figure(figsize=(7, 7))
plt.title('Changes in the Luminosity during the images')
plt.grid(True)

plt.ylabel('# Luminosity')
plt.yscale('linear')

plt.xlabel('# Image')
plt.xticks(xx)

for i in range(o):
    data1 = []
    for j in range(n):
        data1.append(int(data[it]))
        it = it+1
    plt.plot(data1, label=('Object '+str(i+1)))
plt.legend()
# plt.show()
plt.savefig('./luminosity.png')
