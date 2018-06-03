#!/usr/bin/python

from itertools import tee, izip

from cv2 import *
import numpy as np;


def pairwise(iterable):
    "s -> (s0,s1), (s1,s2), (s2, s3), ..."
    a, b = tee(iterable)
    next(b, None)
    return izip(a, b)

capture = VideoCapture("a/%1d.pgm")
video = iter(lambda: capture.read()[1], None)
matcher = BFMatcher(NORM_HAMMING, crossCheck=True)
# tracker = TrackerBoosting_create()
# tracker = TrackerMIL_create()
# tracker = TrackerKCF_create()
# tracker = TrackerTLD_create()
# tracker = TrackerMedianFlow_create()
# tracker = TrackerGOTURN_create()

params = SimpleBlobDetector_Params()
params.blobColor=255
params.minThreshold = 128
params.maxThreshold = 255
params.filterByArea = True
params.minArea = 5
params.filterByCircularity = True
params.minCircularity = 0.65
params.filterByInertia = True
params.minInertiaRatio = 0.65
detector = SimpleBlobDetector(params)
blur = (5, 5)

for old, new in pairwise(video):
    old = GaussianBlur(old, blur, 0)
    new = GaussianBlur(new, blur, 0)
    old_keypoints = detector.detect(old)
    new_keypoints = detector.detect(new)
    # matches = matcher.match(new_keypoints, old_keypoints)
    im_with_keypoints = drawKeypoints(old, old_keypoints, None, flags=DRAW_MATCHES_FLAGS_DRAW_RICH_KEYPOINTS)
    # im_with_keypoints = drawKeypoints(old, old_keypoints, None, (0,0,255), flags=DRAW_MATCHES_FLAGS_DRAW_RICH_KEYPOINTS)

    # print([keypoint.size for keypoint in keypoints])
    # Show blobs
    imshow("Keypoints", im_with_keypoints)
    # input()
    import time
    time.sleep(0.5)
    k = waitKey(30) & 0xff
    if k == 27:
        break
    # waitKey(0)
