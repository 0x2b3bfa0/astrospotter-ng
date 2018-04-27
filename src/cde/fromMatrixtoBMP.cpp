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

vvvi I;

void inputHAND(){
    int n, h, w;
    cout << "Numero de matrius" << endl;
    cin >> n;
    cout << "Tamany de la matriu" << endl;
    cin >> h >> w;

    I = vvvi(n, vvi (h, vi(w, 0)));

    for(int im=0; im<n; im++){
        cout << "Inserta matriu " << im + 1 << endl;
        for(auto &x : I[im]){
            for(auto &y : x) cin >> y;
        }
        cout << endl << endl;
    }
}

void save(){
    int n = I.size();
    int h = I[0].size();
    int w = I[0][0].size();
    for(int k=0; k<n; k++){
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
                int s = I[k][j][i];
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

        char* s = (char*) calloc(1, sizeof(char));
        int len = strlen(s);
        s = (char*) realloc(s,len+11);

        char asd = k+1 + '0';

        *(s+len) = 'i';
        *(s+len+1) = 'm';
        *(s+len+2) = 'a';
        *(s+len+3) = 't';
        *(s+len+4) = 'g';
        *(s+len+5) = 'e';
        *(s+len+6) = asd;
        *(s+len+7) = '.';
        *(s+len+8) = 'b';
        *(s+len+9) = 'm';
        *(s+len+10) = 'p';
        *(s+len+11) = '\0';

        f = fopen(s, "wb");
        fwrite(bmpfileheader,1,14,f);
        fwrite(bmpinfoheader,1,40,f);
        for(int i=0; i<h; i++){
            fwrite(img+(w*(h-i-1)*3),3,w,f);
            fwrite(bmppad,1,(4-(w*3)%4)%4,f);
        }
        fclose(f);
    }
}

int main(){
    inputHAND();

    save();
}
