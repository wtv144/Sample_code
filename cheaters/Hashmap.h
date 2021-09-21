//
// Created by warre on 4/26/2020.
//

#ifndef CHEATERS_HASHMAP_H
#define CHEATERS_HASHMAP_H

#include <vector>
#include <string>
using namespace std;

class Hashmap {

public:
    Hashmap();
    ~Hashmap();
    void insert(int x, string str);
    int hash1(string str);
   int hash(string str);

    struct hashNode
    {
        int index;
        hashNode *next;
    };

    void printnodes();
    void getCollisions(int* counts[]);
    double getpercent();



private:
    static const int SIZE = 166693;
    hashNode *table[SIZE];



};


#endif //CHEATERS_HASHMAP_H
