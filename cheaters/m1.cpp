//
// Created by warre on 4/26/2020.
//
#include <sys/types.h>
#include <dirent.h>
#include <errno.h>
#include <vector>
#include <string>
#include <iostream>
#include <deque>
#include <fstream>
#include "Hashmap.h"
#include <stdlib.h>
#define MIN(a, b) (((a) < (b)) ? (a) : (b))
#define MAX(a, b) (((a) > (b)) ? (a) : (b))
using namespace std;

/*function... might want it in some class?*/
int getdir (string dir, vector<string> &files)
{
    DIR *dp;
    struct dirent *dirp;
    if((dp  = opendir(dir.c_str())) == NULL) {
        cout << "Error(" << errno << ") opening " << dir << endl;
        return errno;
    }

    while ((dirp = readdir(dp)) != NULL) {
        files.push_back(string(dirp->d_name));
    }
    closedir(dp);
    return 0;
}
void printdeque(deque<string> q){
    for(int i = 0; i < q.size(); i++){
        cout << q[i];
    }
    cout << endl;
}
void cleanstr(string s){
  //Optional since the words read are kind of messy
}
void printcollisiontable(int * check[], vector<string> files){
    for(int i = 0; i < files.size();i++){
        cout <<"Row "<< i << "\t";
        for(int j = 0; j < i ; j++){
            cout<< check[i][j] << " ";
        }
        cout<<endl;
    }
}
void outputmatches(int * check[], int tol, vector<string> files){
    int count = 0;
    for(int i = 0; i < files.size(); i++){
        for(int j=0; j <i; j++){
            if( check[i][j]>= tol){
                count++;
                cout << check[i][j] << ": "<< files[i] << " and "<< files[j] << endl;
            }
        }

    }
    cout << "Total pairs: " << count <<endl;
}


int main(int argc, char* argv[])
{
    /*
    string dir = string(argv[1]);
    int n = atoi(argv[2]);
    int tolerance = atoi(argv[3]);
*/

    // Linux string dir = string("sm_doc_set");
    // CHANGE THESE PATHS FOR YOUR COMPUTER ZIYAN

   string small = string("C:\\Users\\warre\\EE312\\Labs\\cheaters\\sm_doc_set"); ///cheaters/sm_doc_set
   string medium = string("C:\\Users\\warre\\EE312\\Labs\\cheaters\\med_doc_set");
   string large = string("C:\\Users\\warre\\EE312\\Labs\\cheaters\\big_doc_set");
   string dir;
   // cout<< "Enter 0 (small set), 1(medium set), or 2(large set) to test: "<<endl;
    int set=0;
    cin >> set;
    if (set == 0){
        dir = small;
    }
   else if(set ==1){
       dir = medium;
   }
   else if(set ==2){
       dir = large;
   }



    vector<string> files = vector<string>();

    getdir(dir,files);
    int n= 6;
  //  cout <<" Input n (the sequence of words): " << endl;
    //cin >> n;
    files.erase(files.begin(), files.begin()+2);        //removes the first two useless elements


    int tolerance= 200;
    /*
    cout << "input the tolerance:";
    cin >> tolerance;
     */


//May not be the same in linux
    ifstream inFile;
    deque<string> m1;
    Hashmap hash =  Hashmap();
    //cout << files.size() << endl;
    for(int i = 0; i < files.size(); i++) {
        string fname = dir + "/" + files[i]; // \\

        //cout << "File " << i << ": \t"<< fname << endl;
        inFile.open(fname.c_str());

        string word;

        //cout << m1.size();
        while (inFile >> word) {
            if (m1.size() < n) {
                m1.push_back(word);
            }

            if (m1.size() == n) {
                m1.pop_front();
                m1.push_back(word);
                //printdeque(m1);

                string h = "";

                for(int j = 0; j<n; j++)
                {
                    h += m1[j];
                }

                //cout<<h<<endl;
                hash.insert(i,h);

            }

        }
        inFile.close();
        m1.clear();
    }

//hash.printnodes();
    int*check[files.size()];
    //make the triangular array
    for(int i =0; i < files.size();i++){
        check[i] = new int[i];

        for(int j = 0; j < i ; j++){
            check[i][j]=0;
        }
    }
    //iterate through everything and get values


    hash.getCollisions(check);
   // printcollisiontable(check, files);
   outputmatches(check, tolerance, files);

    return 0;
}
