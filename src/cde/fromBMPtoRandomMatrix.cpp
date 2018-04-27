#include "lib/BitMap.h"
using namespace std;

typedef pair <int, int> ii;
typedef tuple <int, int, int> iii;
typedef vector <ii> vii;
typedef set <iii> siii;
typedef vector <vii> vvii;
typedef vector <int> vi;
typedef vector <vi> vvi;
typedef vector <vvi> vvvi;

#define endl '\n'

ifstream fin("PATH.txt");
char* PATH1;
char* PATH2;
string IMpath = "\\\\cas\\\\actual\\\\";
int rand_max = 150;

int nxifres(int n){
    if(n == 0) return 1;
    return log10(n) + 1;
}

vvi inputBMP(int im){
    cerr << "Nom de " << im+1 << " la imatge" << endl;
    cerr << "Acaba l'input amb l'extensio i ? " << endl;
    cerr << "Exemple: imatge1.bmp?" << endl << endl;

    char* s = (char*) calloc(0, sizeof(char));
    char t;
    int len;

    ifstream fin2("PATH.txt");
    while(fin2 >> t){
        len = strlen(s);
        s = (char*) realloc(s, len+2);
        *(s+len) = t;
        *(s+len+1) = '\0';
    }
    istringstream fol(IMpath);
    while(fol >> t){
        len = strlen(s);
        s = (char*) realloc(s, len+2);
        *(s+len) = t;
        *(s+len+1) = '\0';
    }

    while(cin >> t and t != '?'){
       len = strlen(s);
       s = (char*) realloc(s, len+2);
       *(s+len) = t;
       *(s+len+1) = '\0';
        //cerr << "EOO " << t << endl;
    }

    len = strlen(s);

    //cerr << s << " " << len << endl;

    cerr << s << endl;

    BitMap imbt(s);
    int w = imbt.height(), h = imbt.width();
    //cerr << endl;
    vvi IT(h, vi(w, 0));
    for(int i=0; i<h; i++){
        for(int j=0; j<w; j++){
                vi T = imbt.getPixel(i, j);
                IT[i][j] = (T[0] + T[1] + T[2])/3;
            //  cerr << "PETA" << endl;
                
            //if(i%10 == 0 and j%10 == 0) 
                //cerr << i << " "<< j << ":  " << T[0] << endl;
        }
    }
    return IT;
}

int main(){
    
    PATH1 = (char*) calloc(0, sizeof(char));
    PATH2 = (char*) calloc(0, sizeof(char));
    char t;
    int len;
    while(fin >> t){
       len = strlen(PATH1);
       PATH1 = (char*) realloc(PATH1, len+2);
       PATH2 = (char*) realloc(PATH2, len+2);
       *(PATH1+len) = t;
       *(PATH2+len) = t;
       *(PATH1+len+1) = '\0';
       *(PATH2+len+1) = '\0';
    }
    istringstream fin1("\\\\fil\\\\fromBMPtoRandomMatrixIN.txt");
    while(fin1 >> t){
       len = strlen(PATH1);
       PATH1 = (char*) realloc(PATH1, len+2);
       *(PATH1+len) = t;
       *(PATH1+len+1) = '\0';
    }
    istringstream fin2("\\\\fil\\\\fromBMPtoRandomMatrixOUT.txt");
    while(fin2 >> t){
       len = strlen(PATH2);
       PATH2 = (char*) realloc(PATH2, len+2);
       *(PATH2+len) = t;
       *(PATH2+len+1) = '\0';
    }

    cerr << "1: " << PATH1 << endl;
    cerr << "2: " << PATH2 << endl;

    freopen(PATH1, "r", stdin);
    freopen(PATH2, "w", stdout);
    //int asd;
    //cin >> asd;
    //default_random_engine generator;
    //poisson_distribution<int> distribution(rand_max/2);

    cerr << "Numero de imatges" << endl;
    int imm;
    cin >> imm;
    cout << imm << endl;
    for(int im = 0; im < imm; im++){
        vvi I = inputBMP(im);

        if(im == 0) cout << I.size() << " " << I[0].size() << endl;
        
        int h = I.size();
        int w = I[0].size();

        for(int i=0; i<h; i++){
            for(int j=0; j<w; j++){
                I[i][j] += rand()%rand_max;
                if(I[i][j] > 255) I[i][j] = 255;
            }
        }

        int max = 0;
        for(int i=0; i<h; i++){
            for(int j=0; j<w; j++) if(max < I[i][j]) max = I[i][j];
        }

        for(int i=0; i<h; i++){
            for(int j=0; j<w; j++){

                for(int s = 0; s<nxifres(max) - nxifres(I[i][j]); s++) cout << " ";

                cout << " " << I[i][j];
            }
            if(i != h-1) cout << endl;

        }
        cout << endl << endl;
    }
}
