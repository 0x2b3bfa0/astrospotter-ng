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

int nxifres(int n){
    if(n == 0) return 1;
    return log10(n) + 1;
}

vvi inputBMP(){
    cout << "Nom de la imatge" << endl;
    cout << "Acaba l'input amb '?' " << endl;

    char* s = (char*) calloc(1, sizeof(char));
    char t;
    int len;
    while(cin >> t and t != '?'){
       len = strlen(s);
       s = (char*) realloc(s, len+1);
       *(s+len) = t;
       *(s+len+1) = '\0';
    }

    len = strlen(s);
    s = (char*) realloc(s,len+5);
    *(s+len) = '.';
    *(s+len+1) = 'b';
    *(s+len+2) = 'm';
    *(s+len+3) = 'p';
    *(s+len+4) = '\0';

    BitMap imbt(s);
    int h = imbt.height(), w = imbt.width();
    cout << endl;
    vvi IT(h, vi(w, 0));
    for(int i=0; i<h; i++){
        for(int j=0; j<w; j++){
            vi T = imbt.getPixel(i, j);
           // cout << i << " "<< j << ":  " << T[0] << endl;
            IT[i][j] = (T[0] + T[1] + T[2])/3;
        }
    }
    return IT;
}

int main(){
    vvi I = inputBMP();
    ofstream fout("Matrix.txt");
    fout << I.size() << " " << I[0].size() << endl;

    int h = I.size();
    int w = I[0].size();


    int max = 0;
    for(int i=0; i<h; i++){
        for(int j=0; j<w; j++) if(max < I[i][j]) max = I[i][j];
    }

    for(int i=0; i<h; i++){
        for(int j=0; j<w; j++){

            for(int s = 0; s<nxifres(max) - nxifres(I[i][j]); s++) fout << " ";

            fout << " " << I[i][j];
        }
        if(i != h-1) fout << endl;
    }
}
