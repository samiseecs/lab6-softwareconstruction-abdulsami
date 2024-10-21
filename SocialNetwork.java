package twitter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets a list of tweets providing the evidence, not modified by this
     *               method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets. One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion. All the
     *         Twitter usernames in the returned social network must be either
     *         authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        
        Pattern mentionPattern = Pattern.compile("@(\\w+)");
        
        for (Tweet tweet : tweets) {
            String author = tweet.getAuthor().toLowerCase(); // Normalize author to lowercase
            Matcher matcher = mentionPattern.matcher(tweet.getText());

            while (matcher.find()) {
                String mentionedUser = matcher.group(1).toLowerCase(); // Normalize mentioned user to lowercase
                
                if (!mentionedUser.equals(author)) {
                    followsGraph.putIfAbsent(author, new HashSet<>());
                    followsGraph.get(author).add(mentionedUser);
                }
            }
        }

        return followsGraph;
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCounts = new HashMap<>();

        for (Set<String> followers : followsGraph.values()) {
            for (String user : followers) {
                followerCounts.put(user, followerCounts.getOrDefault(user, 0) + 1);
            }
        }

        List<String> influencers = new ArrayList<>(followerCounts.keySet());
        influencers.sort((user1, user2) -> followerCounts.get(user2) - followerCounts.get(user1));

        return influencers;
    }
}
