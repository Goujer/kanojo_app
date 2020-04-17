package twitter4j.api;

import twitter4j.Friendship;
import twitter4j.IDs;
import twitter4j.PagableResponseList;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

public interface FriendsFollowersResources {
    User createFriendship(long j) throws TwitterException;

    User createFriendship(long j, boolean z) throws TwitterException;

    User createFriendship(String str) throws TwitterException;

    User createFriendship(String str, boolean z) throws TwitterException;

    User destroyFriendship(long j) throws TwitterException;

    User destroyFriendship(String str) throws TwitterException;

    IDs getFollowersIDs(long j) throws TwitterException;

    IDs getFollowersIDs(long j, long j2) throws TwitterException;

    IDs getFollowersIDs(String str, long j) throws TwitterException;

    PagableResponseList<User> getFollowersList(long j, long j2) throws TwitterException;

    PagableResponseList<User> getFollowersList(String str, long j) throws TwitterException;

    IDs getFriendsIDs(long j) throws TwitterException;

    IDs getFriendsIDs(long j, long j2) throws TwitterException;

    IDs getFriendsIDs(String str, long j) throws TwitterException;

    PagableResponseList<User> getFriendsList(long j, long j2) throws TwitterException;

    PagableResponseList<User> getFriendsList(String str, long j) throws TwitterException;

    IDs getIncomingFriendships(long j) throws TwitterException;

    IDs getOutgoingFriendships(long j) throws TwitterException;

    ResponseList<Friendship> lookupFriendships(long[] jArr) throws TwitterException;

    ResponseList<Friendship> lookupFriendships(String[] strArr) throws TwitterException;

    Relationship showFriendship(long j, long j2) throws TwitterException;

    Relationship showFriendship(String str, String str2) throws TwitterException;

    Relationship updateFriendship(long j, boolean z, boolean z2) throws TwitterException;

    Relationship updateFriendship(String str, boolean z, boolean z2) throws TwitterException;
}
