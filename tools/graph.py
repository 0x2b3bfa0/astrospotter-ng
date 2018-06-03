#!/usr/bin/env python3.6
"""Represent visually the luminosity.txt and center.txt datasets."""

import click
import numpy
import matplotlib.pyplot as plot

with open("./luminosity.txt", 'r') as input:
    objects, frames, *values = map(int, input.read().split())
    luminosity = numpy.array_split(values, frames)

    plot.figure(figsize=(7, 7))
    plot.title('Luminosity variation between frames')

    plot.xlabel('# Frame')
    plot.xticks(range(frames))
    plot.ylabel('# Luminosity')
    plot.yscale('linear')

    plot.grid(True)

    for number, object in enumerate(luminosity, 1):
        plot.plot(object, label=(f'Object {number}'))
    plot.legend()
    plot.show()
    # plot.savefig('./luminosity.png')

import sys
sys.exit(0)




o = int(objects)
n = int(frames)
W = int(width)
H = int(height)

plot.figure(figsize=(7, 7))
plot.title('Moving Centers')
plot.grid(True)

yy = numpy.arange(0, H, 20)
xx = numpy.arange(0, W, 20)

plot.ylabel('H')
plot.yticks(yy)
plot.ylim([0, H])

plot.xlabel('W')
plot.xticks(xx)
plot.xlim([0, W])
it = 0

for i in range(o):
    data1 = []
    data2 = []
    for j in range(n):
        data1.append(float(data[it]))
        it = it+1
        data2.append(H - float(data[it]))
        it = it+1
    plot.plot(data1, data2, 'o', label=('Object '+str(i+1)))
plot.legend()
# plot.show()
plot.savefig("./center.png")




with open("./center.txt", 'r') as centers:
    centers = centers.read().split()
