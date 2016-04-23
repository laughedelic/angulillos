package com.bio4j.angulillos.test;

import com.bio4j.angulillos.*;

import java.net.URL;
import java.util.Date;
import java.util.stream.Stream;


public abstract class Twitter<RV,RE>
extends
  TypedGraph<Twitter<RV,RE>, RV,RE>
{

  public Twitter(UntypedGraph<RV,RE> raw) { super(raw); }

  public abstract class Vertex<
    V extends Vertex<V>
  > extends TypedGraph<Twitter<RV,RE>, RV,RE>.Vertex<V> {

    protected Vertex(RV raw, VertexType<V> type) { super(raw, type); }

    // experimenting with override:
    @Override public <
      E  extends      TypedEdge<V,VertexType<V>, E,ET, ?,?, ?,RV,RE>,
      ET extends TypedEdge.Type<V,VertexType<V>, E,ET, ?,?, ?,RV,RE>
    >
    Stream<E> outE(ET edgeType) {
      System.out.println("This is overriden Twitter-graph specific outE");
      return outE(edgeType);
    }
  }


  /* ### Vertices and their types */

  public final class User extends Vertex<User> {
    @Override public final User self() { return this; }
    private User(RV raw) { super(raw, user); }
  }

  public final UserType user = new UserType();
  public final class UserType extends VertexType<User> {
    public final User fromRaw(RV raw) { return new User(raw); }

    public final name name = new name();
    public final class name extends Property<String> {
      private name() { super(String.class); }
    }

    public final age age = new age();
    public final class age extends Property<Integer> {
      private age() { super(Integer.class); }
    }
  }


  public final class Tweet extends Vertex<Tweet> {
    @Override public final Tweet self() { return this; }
    private Tweet(RV raw) { super(raw, tweet); }
  }

  public final TweetType tweet = new TweetType();
  public final class TweetType extends VertexType<Tweet> {
    public final Tweet fromRaw(RV raw) { return new Tweet(raw); }

    public final text text = new text();
    public final class text extends Property<String> {
      private text() { super(String.class); }
    }

    public final url url = new url();
    public final class url extends Property<URL> {
      private url() { super(URL.class); }
    }
  }


  /* ### Edges and their types */

  public final class Follows extends Edge<User, Follows, User> {
    @Override public final Follows self() { return this; }
    private Follows(RE raw) { super(raw, follows); }
  }

  public final FollowsType follows = new FollowsType();
  public final class FollowsType extends EdgeType<User, Follows, User> implements Arity.AnyToAny {
    private FollowsType() { super(user, user); }
    public final Follows fromRaw(RE raw) { return new Follows(raw); }

    public final since since = new since();
    public final class since extends Property<Date> {
      private since() { super(Date.class); }
    }
  }


  public final class Posted extends Edge<User, Posted, Tweet> {
    @Override public final Posted self() { return this; }
    private Posted(RE raw) { super(raw, posted); }
  }

  public final PostedType posted = new PostedType();
  public final class PostedType extends EdgeType<User, Posted, Tweet> implements Arity.AnyToAny {
    private PostedType() { super(user, tweet); }
    public final Posted fromRaw(RE raw) { return new Posted(raw); }

    public final when when = new when();
    public final class when extends Property<Date> {
      private when() { super(Date.class); }
    }
  }

}