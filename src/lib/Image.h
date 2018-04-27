#include "BitMap.h"
#include <bits/stdc++.h>
using namespace std;

typedef long long ll;
typedef long double ld;
typedef pair <int, int> ii;
typedef tuple <int, int, int> iii;
typedef vector <ii> vii;
typedef set <iii> siii;
typedef vector <vii> vvii;
typedef vector <int> vi;
typedef vector <vi> vvi;
typedef vector <vvi> vvvi;

#define PI acos(-1)
#define INF 1e9
#define MAX_BIT 255

class Image{
	private:
		int lim;
		int X[8] = { 1, 1, 1, 0, 0,-1,-1,-1};
		int Y[8] = {-1, 0, 1,-1, 1,-1, 0, 1};
	
		int ndigits(int n){
			if(n < 0) return ndigits(-n) + 1;
			if(n == 0) return 1;
			return log10(n)+1;
		}

	public:
		int W, H;

		vvi pix;
		vvi pixSeen;

		Image(){};
		
		Image(int w, int h, int L){
			lim = L;
			W = w, H = h;
			pix = vvi(w, vi(h, -1));
			pixSeen = vvi(w, vi(h, 0));
		}

		void inputImage(){
			for(auto &x : pix)
            	for(auto &y : x) 
            		cin >> y;
        }

		void DFSFill(int x, int y, siii &in, siii &ex){
			in.insert(make_tuple(x, y, pix[x][y]));
			pixSeen[x][y] = 1;

			for(int it = 0; it<8; it++){
			    int xn = x + X[it], yn = y + Y[it];
			    if(0 <= xn and xn < W and 0 <= yn and yn < H){
			        if(not (pix[xn][yn] > lim)){
			            ex.insert(make_tuple(x, y, pix[x][y]));
			            in.insert(make_tuple(x, y, pix[x][y]));
			        }
			        else if(pixSeen[xn][yn] == 0)
			            DFSFill(xn, yn, in, ex);
			    }
			}
		}

		void chckIm(){
			for(int j=0; j<H; j++){
				for(int i=0; i<W; i++){
					cerr << string(ndigits(MAX_BIT)+1 - ndigits(pix[i][j]), ' ') << pix[i][j];
				}
				cerr << endl;
			}
		}

		void saveBMP(char* name){
			int h = H;
			int w = W;
			
			FILE *f;
			unsigned char *img = NULL;
			ll filesize = 54 + 3*W*H;
			if(img) free(img);
			img = (unsigned char *)malloc(3*W*H);
			memset(img,0,(unsigned int) sizeof(img));

			for(int i=0; i<W; i++){
			    for(int j=0; j<H; j++){
			        int x = i;
			        int y = j;

			        int s = pix[i][j];
			        int r = s, g = s, b = s;

			        img[(x+y*w)*3+2] = (unsigned char)(r);
			        img[(x+y*w)*3+1] = (unsigned char)(g);
			        img[(x+y*w)*3+0] = (unsigned char)(b);
			    }
			}

			unsigned char bmpfileheader[14] = {'B','M', 0,0,0,0, 0,0, 0,0, 54,0,0,0};
			unsigned char bmpinfoheader[40] = {40,0,0,0, 0,0,0,0, 0,0,0,0, 1,0, 24,0};
			unsigned char bmppad[3] = {0,0,0};

			bmpfileheader[2] = (unsigned char)(filesize);
			bmpfileheader[3] = (unsigned char)(filesize>> 8);
			bmpfileheader[4] = (unsigned char)(filesize>>16);
			bmpfileheader[5] = (unsigned char)(filesize>>24);

			bmpinfoheader[4] = (unsigned char)(w);
			bmpinfoheader[5] = (unsigned char)(w>>8);
			bmpinfoheader[6] = (unsigned char)(w>>16);
			bmpinfoheader[7] = (unsigned char)(w>>24);
			bmpinfoheader[8] = (unsigned char)(h);
			bmpinfoheader[9] = (unsigned char)(h>>8);
			bmpinfoheader[10] = (unsigned char)(h>>16);
			bmpinfoheader[11] = (unsigned char)(h>>24);

			f = fopen(name, "wb");
			fwrite(bmpfileheader,1,14,f);
			fwrite(bmpinfoheader,1,40,f);
			for(ll i=0; i<h; i++){
			    fwrite(img+(w*(h-i-1)*3),3,w,f);
			    fwrite(bmppad,1,(4-(w*3)%4)%4,f);
			}
			fclose(f);
		}
};
