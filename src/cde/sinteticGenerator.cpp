#include "ASLib.h"
using namespace std;

typedef pair <int, int> ii;
typedef tuple <int, int, int> iii;
typedef vector <ii> vii;
typedef set <iii> siii;
typedef vector <vii> vvii;
typedef vector <int> vi;
typedef vector <vi> vvi;
typedef vector <vvi> vvvi;

vvi I;
int X[4] = {1, 0, -1, 0};
int Y[4] = {0, -1, 0, 1};

void output(){
    int h = I.size();
    int w = I[0].size();

    FILE *f;
    unsigned char *img = NULL;
    int filesize = 54 + 3*w*h;
    if(img) free(img);
    img = (unsigned char *)malloc(3*w*h);
    memset(img,0,sizeof(img));

    for(int i=0; i<w; i++){
        for(int j=0; j<h; j++){
            int x = i;
            int y = (h-1)-j;
            int s = I[i][j];
            int r=s, g=s, b=s;

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

    f = fopen("sintenticOUT.bmp", "wb");
    fwrite(bmpfileheader,1,14,f);
    fwrite(bmpinfoheader,1,40,f);
    for(int i=0; i<h; i++){
        fwrite(img+(w*(h-i-1)*3),3,w,f);
        fwrite(bmppad,1,(4-(w*3)%4)%4,f);
    }
    fclose(f);
}

int main(){
    srand(time(NULL));
    I = vvi(100, vi(100, 0));

    for(int i=0; i<4; i++){
        int xi = rand()%100;
        int yi = rand()%100;
        int r = rand()%50;

        queue<iii> Q;
        Q.emplace(xi, yi, 255);
        I[xi][yi] = 255;
        while(not Q.empty()){
            iii aux = Q.front();
            Q.pop();
            int xa = get<0>(aux);
            int ya = get<1>(aux);
            int na = get<2>(aux);
            for(int k=0; k<4; k++){
                int xn = xa + X[k];
                int yn = ya + Y[k];

                if(0 <= xn and xn < 100 and 0 <= yn and yn < 100 and I[xn][yn] == 0){
                    I[xn][yn] = max(na - r, 0);
                    if(I[xn][yn]!=0) Q.emplace(xn, yn, na-r);
                }
            }
        }
    }
    for(auto x : I){
        for(auto y : x) cout << y << " ";
        cout << endl;
    }
    output();
}
