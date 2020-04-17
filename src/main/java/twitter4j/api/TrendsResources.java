package twitter4j.api;

import twitter4j.GeoLocation;
import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Trends;
import twitter4j.TwitterException;

public interface TrendsResources {
    ResponseList<Location> getAvailableTrends() throws TwitterException;

    ResponseList<Location> getAvailableTrends(GeoLocation geoLocation) throws TwitterException;

    ResponseList<Location> getClosestTrends(GeoLocation geoLocation) throws TwitterException;

    Trends getLocationTrends(int i) throws TwitterException;

    Trends getPlaceTrends(int i) throws TwitterException;
}
