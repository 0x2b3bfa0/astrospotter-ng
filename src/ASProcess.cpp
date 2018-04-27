#include "lib/BitMap.h"
#include "lib/Object.h"
#include "lib/Image.h"
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

string PATH;
char* PATH1;
char* PATH2;

int lim = 250;

vector <Image>  I;
vector <Object> O;
Image Stack;

void paths();
void init();
void inputHand();
void tagObjectsNeedle();
void movingCenters();
void makeStack();
void output();

int main(){
	cout.setf(ios::fixed);
	cout.precision(4);

	paths();

	init();
	inputHand();
    cerr << "INPUT DONE" << endl;

    tagObjectsNeedle();
    cerr << "TAGGINE DONE" << endl;

    movingCenters();
    cerr << "REGRESSIONS DONE" << endl;
    
    makeStack();
	cerr << "STACK DONE" << endl;

	output();
}

void paths(){
    ifstream fin("PATH.txt");

    PATH1 = (char*) calloc(0, sizeof(char));
    PATH2 = (char*) calloc(0, sizeof(char));
    char t;
    int len;
    while(fin >> t){
       PATH += t;

       len = strlen(PATH1);
       PATH1 = (char*) realloc(PATH1, len+2);
       PATH2 = (char*) realloc(PATH2, len+2);
       *(PATH1+len) = t;
       *(PATH2+len) = t;
       *(PATH1+len+1) = '\0';
       *(PATH2+len+1) = '\0';
    }
    istringstream fin1("\\fil\\matrices.txt");
    while(fin1 >> t){
       len = strlen(PATH1);
       PATH1 = (char*) realloc(PATH1, len+2);
       *(PATH1+len) = t;
       *(PATH1+len+1) = '\0';
    }
    istringstream fin2("\\fil\\process.txt");
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
}

void init(){
	int n, w, h;
    cerr << "Number of images (n)" << endl;
    cin >> n;
    cerr << "Size of the images (W x H)" << endl;
    cin >> w >> h;

    I 		= vector <Image>  (n, Image(w, h, lim));
    O 		= vector <Object> ();
    Stack 	= Image(w, h, 2*lim/n);
}

void inputHand(){
    for(int i = 0; (unsigned int) i < I.size(); i++){
        cerr << "Insert image " << i+1 << endl;
        I[i].inputImage();
        cerr << endl << endl;
    }
}

void tagObjectsNeedle(){
    for(int i = 1; i+1 < I[0].W; i++){
        for(int j = 1; j+1 < I[0].H; j++){
            int s = 0;
            s += I[0].pix[i-1][j-1];
            s += I[0].pix[ i ][j-1];
            s += I[0].pix[i+1][j-1];

            s += I[0].pix[i-1][ j ];
            s += I[0].pix[ i ][ j ];
            s += I[0].pix[i+1][ j ];

            s += I[0].pix[i-1][j+1];
            s += I[0].pix[ i ][j+1];
            s += I[0].pix[i+1][j+1];

            s /= 9;

            if(s > lim and I[0].pix[i][j] > lim and I[0].pixSeen[i][j] == 0){
                O.push_back(Object(I.size()));
                I[0].DFSFill(i, j, O[O.size()-1].inObj[0], O[O.size()-1].exObj[0]);
            }
        }
    }

    for(auto &x : O) x.findCenter(0);
    for(auto &x : O) x.findLum(0);

    //cerr << "MIDE : " << O[0].lum[0] << endl;

    for(int k = 1; (unsigned int) k < I.size(); k++){
        //cerr << "ABOUT TO NEED" << k << endl;
        for(int i = 0; (unsigned int) i < O.size(); i++){
            int cx = O[i].center[k-1].first;
            int cy = O[i].center[k-1].second;
            
            //cerr << cx << " " << cy << endl;
            //cerr << "Con luminosidad " << I[k].pix[cx][cy] << endl;

            if(I[k].pix[cx][cy] > lim){
                //cerr << "CENTERS TOCH" << endl;
                I[k].DFSFill(cx, cy, O[i].inObj[k], O[i].exObj[k]);
            }
            else{
                //cerr << "CENTERS DON?T TOCH" << endl;
                for(auto x : O[i].inObj[k-1]){
                    cx = get<0>(x), cy = get<1>(x);
                    if(I[k].pix[cx][cy] > lim){
                        I[k].DFSFill(cx, cy, O[i].inObj[k], O[i].exObj[k]);
                        break;
                    }
                }
            }
        }
        for(auto &x : O) x.findCenter(k);
        for(auto &x : O) x.findLum(k);
        //cerr << "NEED & LUM" << k << endl;
    }
}

void movingCenters(){
	for(auto &x : O){
		x.findSpeedDecl();
	}
}

void makeStack(){
	int S = I.size();
    //
    cerr << "MAKING STACK";
	for(int i=0; i < Stack.W; i++){
		for(int j=0; j < Stack.H; j++){
			int s = 0;
			for(int k = 0; (unsigned int) k < I.size(); k++) s += I[k].pix[i][j];
			s /= S;
			Stack.pix[i][j] = min(s, MAX_BIT);
		}
	}

	lim /= I.size();
	for(int i = 0; (unsigned int) i < O.size(); i++){
        int cx = O[i].center[0].first;
        int cy = O[i].center[0].second;
	    if(Stack.pix[cx][cy] > lim) Stack.DFSFill(cx, cy, O[i].inStack, O[i].exStack);
    }
    lim *= I.size();

	for(auto &x : O) x.eccenStack = x.findEccen();
}

void output(){
	cout << O.size() << "," << endl;
    for(auto x : O){
    	if(x.decl == 0 and x.speed == 0) cout << "0, 0," << endl;
   		else cout << x.decl << ", " << x.speed << "," << endl;
    }
    for(auto x : O) 
    	cout << x.eccenStack << "," << endl;


    Stack.saveBMP((char*) ("pic/stack.bmp"));
   	for(int i=0; (unsigned int) i<I.size(); i++){
   		
   		string na = "pic/";
   		na += ('0' + i);
   		na += ".bmp";

   		char* s = (char*) calloc(0, sizeof(char));
    	char t;
    	int len;
    	istringstream sin(na);
	   	while(sin >> t){
	       	len = strlen(s);
	    	s = (char*) realloc(s, len+2);
	       	*(s+len) = t;
	       	*(s+len+1) = '\0';
	       
	    }
   		I[i].saveBMP((char*) s);
	}

	string lumPath = PATH + "\\fil\\lum.txt";
	cerr << lumPath << endl;

	ofstream lout(lumPath);
	lout << O.size() << "," << I.size() << ",";
	for(auto x : O) for(auto y : x.lum) lout << y << ",";

	string cPath = PATH + "\\fil\\center.txt";
	cerr << cPath << endl;

	ofstream cfout(cPath);
	cfout << O.size() << "," << I.size() << "," << I[0].W << "," << I[0].H << ",";
	for(auto x : O) for(auto y : x.center) cfout << y.first << "," << y.second << ",";
}