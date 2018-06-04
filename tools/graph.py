#!/usr/bin/env python3.6
import click
import numpy
import matplotlib.pyplot as graph


@click.group()
def main():
    """
    Represent visually the luminosity.txt and center.txt files.
    """
    pass


@main.command('luminosity', help="Represent visually the luminosity.txt file.")
@click.argument('input', type=click.File('r'))
@click.option('--output', type=click.File('wb'))
def luminosity(input, output):
    objects, frames, *values = map(int, input.read().split())
    objects = numpy.array(values).reshape(objects, frames)

    graph.figure(figsize=(7, 7))
    graph.title('Luminosity variation between frames')
    graph.grid(True)

    graph.xticks(range(frames))
    graph.xlabel('# Frame')
    graph.ylabel('# Luminosity')

    for number, object in enumerate(objects, 1):
        graph.plot(object, label=(f'Object {number}'))
    graph.legend()

    if output is None:
        graph.show()
    else:
        graph.savefig(output)


@main.command('center', help="Represent visually the center.txt file.")
@click.argument('input', type=click.File('r'))
@click.option('--output', type=click.File('wb'))
def center(input, output):
    objects, frames, width, height, *values = map(float, input.read().split())
    objects = numpy.array(values).reshape(int(objects), int(frames), 2)

    graph.figure(figsize=(7, 7))
    graph.title('Moving centers')
    graph.grid(True)

    graph.xlabel('X')
    graph.xticks(numpy.arange(0, width, 20))
    graph.xlim([0, width])

    graph.ylabel('Y')
    graph.yticks(numpy.arange(0, height, 20))
    graph.ylim([height, 0]) # FIXME: flipped Y axis for compatibility

    for number, frames in enumerate(objects, 1):
        graph.plot(*zip(*frames), 'o', label=(f'Object {number}'))
    graph.legend()

    if output is None:
        graph.show()
    else:
        graph.savefig(output)


if __name__ == '__main__':
    main()
