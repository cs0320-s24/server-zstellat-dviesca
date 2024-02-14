package edu.brown.cs.student.main.csvUtilities.Star;

/**
 * Record holding info about Star object
 *
 * @param starID integer of star ID
 * @param properName String of properName
 * @param x int x coordinate
 * @param y int y coordinate
 * @param z int z coordinate
 */
public record Star(int starID, String properName, int x, int y, int z) {}
