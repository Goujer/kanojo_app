package twitter4j.api;

import java.io.File;
import java.io.InputStream;
import twitter4j.AccountSettings;
import twitter4j.IDs;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

public interface UsersResources {
    User createBlock(long j) throws TwitterException;

    User createBlock(String str) throws TwitterException;

    User destroyBlock(long j) throws TwitterException;

    User destroyBlock(String str) throws TwitterException;

    AccountSettings getAccountSettings() throws TwitterException;

    IDs getBlocksIDs() throws TwitterException;

    IDs getBlocksIDs(long j) throws TwitterException;

    PagableResponseList<User> getBlocksList() throws TwitterException;

    PagableResponseList<User> getBlocksList(long j) throws TwitterException;

    ResponseList<User> getContributees(long j) throws TwitterException;

    ResponseList<User> getContributees(String str) throws TwitterException;

    ResponseList<User> getContributors(long j) throws TwitterException;

    ResponseList<User> getContributors(String str) throws TwitterException;

    ResponseList<User> lookupUsers(long[] jArr) throws TwitterException;

    ResponseList<User> lookupUsers(String[] strArr) throws TwitterException;

    void removeProfileBanner() throws TwitterException;

    ResponseList<User> searchUsers(String str, int i) throws TwitterException;

    User showUser(long j) throws TwitterException;

    User showUser(String str) throws TwitterException;

    AccountSettings updateAccountSettings(Integer num, Boolean bool, String str, String str2, String str3, String str4) throws TwitterException;

    User updateProfile(String str, String str2, String str3, String str4) throws TwitterException;

    User updateProfileBackgroundImage(File file, boolean z) throws TwitterException;

    User updateProfileBackgroundImage(InputStream inputStream, boolean z) throws TwitterException;

    void updateProfileBanner(File file) throws TwitterException;

    void updateProfileBanner(InputStream inputStream) throws TwitterException;

    User updateProfileColors(String str, String str2, String str3, String str4, String str5) throws TwitterException;

    User updateProfileImage(File file) throws TwitterException;

    User updateProfileImage(InputStream inputStream) throws TwitterException;

    User verifyCredentials() throws TwitterException;
}
