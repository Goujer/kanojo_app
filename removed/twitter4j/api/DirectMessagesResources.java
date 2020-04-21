package twitter4j.api;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.TwitterException;

public interface DirectMessagesResources {
    DirectMessage destroyDirectMessage(long j) throws TwitterException;

    ResponseList<DirectMessage> getDirectMessages() throws TwitterException;

    ResponseList<DirectMessage> getDirectMessages(Paging paging) throws TwitterException;

    ResponseList<DirectMessage> getSentDirectMessages() throws TwitterException;

    ResponseList<DirectMessage> getSentDirectMessages(Paging paging) throws TwitterException;

    DirectMessage sendDirectMessage(long j, String str) throws TwitterException;

    DirectMessage sendDirectMessage(String str, String str2) throws TwitterException;

    DirectMessage showDirectMessage(long j) throws TwitterException;
}
