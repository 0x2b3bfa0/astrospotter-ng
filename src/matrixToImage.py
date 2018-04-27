from PIL import Image, ImageDraw

inf = open('textImage.txt', 'r').read()
inm = inf.split(' ')

it = 0

W = int(inm[it])
it = it+1

H = int(inm[it])
it = it+1

img = Image.new('RGB', (W, H), 'White')
dib = ImageDraw.Draw(img)

for i in range(W):
	for j in range(H):
		n = int(inm[it])
		it = it+1
		dib.point((i, j), (n, n, n))
		
img.save('newImage.bmp')
