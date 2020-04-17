package twitter4j;

public interface EntitySupport {
    HashtagEntity[] getHashtagEntities();

    MediaEntity[] getMediaEntities();

    URLEntity[] getURLEntities();

    UserMentionEntity[] getUserMentionEntities();
}
