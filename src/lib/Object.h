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

class Object{
    private:
        double aproxAcc = 0.5;
        double imTime = 10.0;
        
        bool aproxEquals(double a, double b){
            if(b-aproxAcc < a and a < b+aproxAcc) return true;
            return false;
        }

    public:
        int N;
        vector <int> lum;
        vector <pair <ld, ld>> center;
        vector <siii> inObj, exObj;
        siii inStack, exStack;
        double eccenStack;
        double speed, decl;

        Object(int n){
            N = n;
            lum = vector <int> (n, 0);
            center = vector <pair <ld, ld>> (n, {-1, -1});
            inObj = exObj = vector <siii> (n);
        }
        
        void append(Object k){           
            for(int i=0; i<k.N; i++){
                k.findCenter(i);
                k.findLum(i);

                center.push_back(k.center[i]);
                lum.push_back(k.lum[i]);
                inObj.push_back(k.inObj[i]);
                exObj.push_back(k.exObj[i]);

            }
            N += k.N;
        }

        void findCenter(int k){
            int mx=0, my=0, in=0;
            for(auto x : inObj[k]){
                mx += get<0>(x)*get<2>(x);
                my += get<1>(x)*get<2>(x);
                in += get<2>(x);
            }
            if(inObj[k].size() == 0 or in == 0) ;//cerr << in << " peta" << endl;
            else{
                //cerr << mx/in << " " << my/in << endl;
                center[k] = {(mx/1.0)/in, (my/1.0)/in};
            }
        } 

        void findLum(int k){
            int l = 0;
            for(auto x : inObj[k]) l += get<2>(x);
            lum[k] = l/MAX_BIT;
        }

        double findEccen(){
            double A = inStack.size();
            double R = -INF;
            for(auto x : exStack){
                for(auto y : exStack){
                    double d = hypot(get<0>(x) - get<0>(y), get<1>(x) - get<1>(y));
                    if(d > R) R = d;
                }
            }
            R /= 2;
            double r = A/(PI*R);
            return R/r;
        }

        void findSpeedDecl(){
            double cix = center[0].first;
            double ciy = center[0].second;

            double cfx = center[N-1].first;
            double cfy = center[N-1].second;

            if(aproxEquals(cix, cfx) and aproxEquals(ciy, cfy)){
                speed = 0, decl = 0;
            }
            else{
                if(cix > cfx and ciy > cfy){
                    ld angle = atan((cix - cfx)/(ciy - cfy));
                    angle *= 180/PI;
                    decl = 360 - angle;
                }
                else if(cix < cfx and ciy < cfy){
                    ld angle = atan((cfx - cix)/(cfy - ciy));
                    angle *= 180/PI;
                    decl = 180 - angle;
                }
                else if(cix > cfx and ciy < cfy){
                    ld angle = atan((cix - cfx)/(cfy - ciy));
                    angle *= 180/PI;
                    decl = 180 + angle;
                }
                else if(cix < cfx and ciy > cfy){
                    ld angle = atan((cfx - cix)/(ciy - cfy));
                    angle *= 180/PI;
                    decl = angle;
                }

                speed = sqrt((cix - cfx)*(cix - cfx) + (ciy - cfy)*(ciy - cfy));
                speed /= N*imTime;
            }
        }
};
