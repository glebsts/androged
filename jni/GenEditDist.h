/*
 * GenEditDistance.h
 *
 *  Created on: Dec 21, 2012
 *      Author: gleb
 *  NB! Build using at least r7 version Crystax 
 */

#ifndef GENEDITDIST_H_
#define GENEDITDIST_H_
#include "FindEditDistanceMod.h"

typedef struct {
	char *result;
	double distance;
	int type;
} SRES;



wchar_t *extractBlockedRegions(wchar_t *searchString, int *searchStringLen);
int findDistances(char *file, wchar_t *string, int stringLen, double editD, char flagsInPositions[FP_MAX_POSITIONS]);
int findBest(char *file, wchar_t *string, int stringLen, int best, char flag);
int getResults(JNIEnv* pEnv, Store* pStore);

#endif /* GENEDITDIST_H_ */
