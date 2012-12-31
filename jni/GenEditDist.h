/*
 * GenEditDistance.h
 *
 *  Created on: Dec 31, 2012
 *      Author: gleb
 */

#ifndef GENEDITDIST_H_
#define GENEDITDIST_H_
#include "FindEditDistanceMod.h"

wchar_t *extractBlockedRegions(wchar_t *searchString, int *searchStringLen);
int findDistances(char *file, wchar_t *string, int stringLen, double editD, char flagsInPositions[FP_MAX_POSITIONS]);
int findBest(char *file, wchar_t *string, int stringLen, int best, char flag);
int doAll(/*int argc, char* argv[] */);

#endif /* GENEDITDIST_H_ */
