package twitter;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class SocialNetworkTest {

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    @Test
    public void testSingleMention() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "alice", "Hello @bob", null));
        
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected alice to follow bob", followsGraph.get("alice").contains("bob"));
    }

    @Test
    public void testMultipleMentions() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "alice", "Hello @bob and @charlie", null));
        
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected alice to follow bob", followsGraph.get("alice").contains("bob"));
        assertTrue("expected alice to follow charlie", followsGraph.get("alice").contains("charlie"));
    }

    @Test
    public void testMultipleTweetsFromOneUser() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "alice", "Hello @bob", null));
        tweets.add(new Tweet(2, "alice", "Hi again @charlie", null));
        
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected alice to follow bob", followsGraph.get("alice").contains("bob"));
        assertTrue("expected alice to follow charlie", followsGraph.get("alice").contains("charlie"));
    }

    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected empty list", influencers.isEmpty());
    }

    @Test
    public void testSingleUserWithoutFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("bob", new HashSet<>());
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected no influencers", influencers.isEmpty());
    }

    @Test
    public void testSingleInfluencer() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Set<String> followers = new HashSet<>();
        followers.add("bob");
        followsGraph.put("alice", followers);
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("expected alice to be the top influencer", "alice", influencers.get(0));
    }

    @Test
    public void testMultipleInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Set<String> followers1 = new HashSet<>();
        followers1.add("bob");
        Set<String> followers2 = new HashSet<>();
        followers2.add("charlie");
        
        followsGraph.put("alice", followers1);
        followsGraph.put("bob", followers2);
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("expected alice to be the top influencer", "alice", influencers.get(0));
        assertEquals("expected bob to be the second influencer", "bob", influencers.get(1));
    }

    @Test
    public void testTiedInfluence() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Set<String> followers1 = new HashSet<>();
        followers1.add("bob");
        Set<String> followers2 = new HashSet<>();
        followers2.add("charlie");
        
        followsGraph.put("alice", followers1);
        followsGraph.put("charlie", followers2);
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("expected alice to be the top influencer", "alice", influencers.get(0));
        assertEquals("expected charlie to be tied as an influencer", "charlie", influencers.get(1));
    }
}
