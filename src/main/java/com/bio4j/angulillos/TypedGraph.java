package com.bio4j.angulillos;

import java.util.List;
import java.util.Optional;
import java.util.LinkedList;
import static com.bio4j.angulillos.conversions.*;

import java.util.stream.Stream;
import java.util.Iterator;

/*
  ## Typed graphs

  A `TypedGraph` is, unsurprisingly, the typed version of [UntypedGraph](UntypedGraph.java.md).
*/
interface TypedGraph <
  G extends TypedGraph<G,I,RV,RVT,RE,RET>,
  I extends UntypedGraph<RV,RVT,RE,RET>, RV,RVT, RE,RET
>
{

  I raw();

  default <
    V extends TypedVertex<V,VT,G,I,RV,RVT,RE,RET>,
    VT extends TypedVertex.Type<V,VT,G,I,RV,RVT,RE,RET>
  >
  V addVertex(VT vertexType) {

    return vertexType.vertex(
      raw().addVertex( vertexType.raw() )
    );
  }

  /* adds an edge; note that this method does not set any properties. As it needs to be called by vertices in possibly different graphs, all the graph bounds are free with respect to G. */
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,RV,RVT,RE,RET>,
    ST extends TypedVertex.Type<S,ST,SG,I,RV,RVT,RE,RET>,
    SG extends TypedGraph<SG,I,RV,RVT,RE,RET>,
    // rel
    R extends TypedEdge<S,ST,SG,R,RT,RG,I,RV,RVT,RE,RET,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,RG,I,RV,RVT,RE,RET,T,TT,TG>,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // tgt
    T extends TypedVertex<T,TT,TG,I,RV,RVT,RE,RET>,
    TT extends TypedVertex.Type<T,TT,TG,I,RV,RVT,RE,RET>,
    TG extends TypedGraph<TG,I,RV,RVT,RE,RET>
  >
  R addEdge(S from, RT relType, T to) {

    return relType.edge(
      raw().addEdge( from.raw(), relType.raw(), to.raw() )
    );
  }

  /*
    ### properties

    These methods are used for setting and getting properties on vertices and edges.
  */
  default <
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>,
    P extends Property<N,NT,P,V,G,I,RV,RVT,RE,RET>,
    V
  >
  V getProperty(N node, P property) {

    return raw().<V>getPropertyV(node.raw(), property.name());
  }

  /* Get the value of a property from an edge of G. */
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,RV,RVT,RE,RET>,
    ST extends TypedVertex.Type<S,ST,SG,I,RV,RVT,RE,RET>,
    SG extends TypedGraph<SG,I,RV,RVT,RE,RET>,
    // rel
    R extends TypedEdge<S,ST,SG,R,RT,G,I,RV,RVT,RE,RET,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,RV,RVT,RE,RET,T,TT,TG>,
    // tgt
    T extends TypedVertex<T,TT,TG,I,RV,RVT,RE,RET>,
    TT extends TypedVertex.Type<T,TT,TG,I,RV,RVT,RE,RET>,
    TG extends TypedGraph<TG,I,RV,RVT,RE,RET>,
    P extends Property<R,RT,P,V,G,I,RV,RVT,RE,RET>,
    V
  >
  V getProperty(R edge, P property) {

    return raw().<V>getPropertyE(edge.raw(), property.name());
  }

  /* Sets the value of a property for a vertex of G. */
  default <
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>,
    P extends Property<N,NT,P,V,G,I,RV,RVT,RE,RET>,
    V
  >
  G setProperty(N node, P property, V value) {

    raw().setPropertyV(node.raw(), property.name(), value);
    return node.graph();
  }

  /* Sets the value of a property for an edge of G. */
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,RV,RVT,RE,RET>,
    ST extends TypedVertex.Type<S,ST,SG,I,RV,RVT,RE,RET>,
    SG extends TypedGraph<SG,I,RV,RVT,RE,RET>,
    // rel
    R extends TypedEdge<S,ST,SG,R,RT,G,I,RV,RVT,RE,RET,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,RV,RVT,RE,RET,T,TT,TG>,
    // tgt
    T extends TypedVertex<T,TT,TG,I,RV,RVT,RE,RET>,
    TT extends TypedVertex.Type<T,TT,TG,I,RV,RVT,RE,RET>,
    TG extends TypedGraph<TG,I,RV,RVT,RE,RET>,
    P extends Property<R,RT,P,V,G,I,RV,RVT,RE,RET>,
    V
  >
  G setProperty(R edge, P property, V value) {

    raw().setPropertyE(edge.raw(), property.name(), value);
    return edge.graph();
  }

  /*
    ### source and target

    gets the source of an edge of G, which could be of a different graph.
  */
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,RV,RVT,RE,RET>,
    ST extends TypedVertex.Type<S,ST,SG,I,RV,RVT,RE,RET>,
    SG extends TypedGraph<SG,I,RV,RVT,RE,RET>,
    // rel
    R extends TypedEdge<S,ST,SG,R,RT,G,I,RV,RVT,RE,RET,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,RV,RVT,RE,RET,T,TT,TG>,
    // tgt
    T extends TypedVertex<T,TT,TG,I,RV,RVT,RE,RET>,
    TT extends TypedVertex.Type<T,TT,TG,I,RV,RVT,RE,RET>,
    TG extends TypedGraph<TG,I,RV,RVT,RE,RET>
  >
  S source(R edge) {

    return edge.type().sourceType().vertex(
      raw().source(edge.raw())
    );
  }

  default <
    // src
    S extends TypedVertex<S,ST,SG,I,RV,RVT,RE,RET>,
    ST extends TypedVertex.Type<S,ST,SG,I,RV,RVT,RE,RET>,
    SG extends TypedGraph<SG,I,RV,RVT,RE,RET>,
    // rel
    R extends TypedEdge<S,ST,SG,R,RT,G,I,RV,RVT,RE,RET,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,RV,RVT,RE,RET,T,TT,TG>,
    // tgt
    T extends TypedVertex<T,TT,TG,I,RV,RVT,RE,RET>,
    TT extends TypedVertex.Type<T,TT,TG,I,RV,RVT,RE,RET>,
    TG extends TypedGraph<TG,I,RV,RVT,RE,RET>
  >
  T target(R edge) {

    return edge.type().targetType().vertex(
      raw().target(edge.raw())
    );
  }


  /* ### Incident edges from vertices */

  /*
    #### out methods

    gets the out edges of a vertex N of G.
  */
  default <
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>,
    //rel
    R extends TypedEdge<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG>,
    RT extends TypedEdge.Type<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG>,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // target node
    T extends TypedVertex<T,TT,TG,I,RV,RVT,RE,RET>,
    TT extends TypedVertex.Type<T,TT,TG,I,RV,RVT,RE,RET>,
    TG extends TypedGraph<TG,I,RV,RVT,RE,RET>
  >
  Stream<R> outE(N node, RT relType) {

    return raw().outE(
      node.raw(),
      relType.raw()
    ).map(
      relType::edge
    );
  }

  default <
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>,
    //rel
    R extends TypedEdge<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG>,
    RT extends TypedEdge.Type<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG>,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // target node
    T extends TypedVertex<T,TT,TG,I,RV,RVT,RE,RET>,
    TT extends TypedVertex.Type<T,TT,TG,I,RV,RVT,RE,RET>,
    TG extends TypedGraph<TG,I,RV,RVT,RE,RET>
  >
  Stream<T> outV(N node, RT relType) {

    return raw().outV (
      node.raw(),
      relType.raw()
    ).map(
      relType.targetType()::vertex
    );
  }

  default <
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>,
    //rel
    R extends TypedEdge<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG>,
    RT extends TypedEdge.Type<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG> &
      TypedEdge.Type.ToOne,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // target node
    T extends TypedVertex<T,TT,TG,I,RV,RVT,RE,RET>,
    TT extends TypedVertex.Type<T,TT,TG,I,RV,RVT,RE,RET>,
    TG extends TypedGraph<TG,I,RV,RVT,RE,RET>
  >
  R outOneE(N node, RT relType) {

    // we know it has one!
    return relType.edge(
      raw().outE(
        node.raw(),
        relType.raw()
      )
      .findFirst().get()
    );
  }

  default <
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>,
    //rel
    R extends TypedEdge<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG>,
    RT extends TypedEdge.Type<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG> &
      TypedEdge.Type.ToOne,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // target node
    T extends TypedVertex<T,TT,TG,I,RV,RVT,RE,RET>,
    TT extends TypedVertex.Type<T,TT,TG,I,RV,RVT,RE,RET>,
    TG extends TypedGraph<TG,I,RV,RVT,RE,RET>
  >
  T outOneV(N node, RT relType) {

    return relType.targetType().vertex(
      raw().outV(
        node.raw(),
        relType.raw()
      )
      .findFirst().get()
    );
  }

  default <
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>,
    //rel
    R extends TypedEdge<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG>,
    RT extends TypedEdge.Type<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG> &
      TypedEdge.Type.ToAtMostOne,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // target node
    T extends TypedVertex<T,TT,TG,I,RV,RVT,RE,RET>,
    TT extends TypedVertex.Type<T,TT,TG,I,RV,RVT,RE,RET>,
    TG extends TypedGraph<TG,I,RV,RVT,RE,RET>
  >
  Optional<R> outOptionalE(N node, RT relType) {

    return raw().outE(
      node.raw(),
      relType.raw()
    )
    .findFirst()
    .map(
      relType::edge
    );
  }
  default <
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>,
    //rel
    R extends TypedEdge<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG>,
    RT extends TypedEdge.Type<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG> &
      TypedEdge.Type.ToAtMostOne,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // target node
    T extends TypedVertex<T,TT,TG,I,RV,RVT,RE,RET>,
    TT extends TypedVertex.Type<T,TT,TG,I,RV,RVT,RE,RET>,
    TG extends TypedGraph<TG,I,RV,RVT,RE,RET>
  >
  Optional<T> outOptionalV(N node, RT relType) {

    return raw().outV(
      node.raw(),
      relType.raw()
    )
    .findFirst()
    .map(
      relType.targetType()::vertex
    );
  }

  default <
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>,
    //rel
    R extends TypedEdge<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG>,
    RT extends TypedEdge.Type<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG> &
      TypedEdge.Type.ToAtLeastOne,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // target node
    T extends TypedVertex<T,TT,TG,I,RV,RVT,RE,RET>,
    TT extends TypedVertex.Type<T,TT,TG,I,RV,RVT,RE,RET>,
    TG extends TypedGraph<TG,I,RV,RVT,RE,RET>
  >
  Stream<R> outManyE(N node, RT relType) {

    return raw().outE(
      node.raw(),
      relType.raw()
    ).map(
      relType::edge
    );
  }
  default <
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>,
    //rel
    R extends TypedEdge<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG>,
    RT extends TypedEdge.Type<N,NT,G, R,RT,RG,I,RV,RVT,RE,RET, T,TT,TG> &
      TypedEdge.Type.ToAtLeastOne,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // target node
    T extends TypedVertex<T,TT,TG,I,RV,RVT,RE,RET>,
    TT extends TypedVertex.Type<T,TT,TG,I,RV,RVT,RE,RET>,
    TG extends TypedGraph<TG,I,RV,RVT,RE,RET>
  >
  Stream<T> outManyV(N node, RT relType) {

    return raw().outV(
      node.raw(),
      relType.raw()
    ).map(
      relType.targetType()::vertex
    );
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /* #### in methods
  */
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,RV,RVT,RE,RET>,
    ST extends TypedVertex.Type<S,ST,SG,I,RV,RVT,RE,RET>,
    SG extends TypedGraph<SG,I,RV,RVT,RE,RET>,
    // rel
    R extends TypedEdge<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G>,
    RT extends TypedEdge.Type<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G>,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // tgt
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>
  >
  Stream<R> inE(RT relType, N node) {

    return raw().inE(
      node.raw(),
      relType.raw()
    ).map(
      relType::edge
    );
  }
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,RV,RVT,RE,RET>,
    ST extends TypedVertex.Type<S,ST,SG,I,RV,RVT,RE,RET>,
    SG extends TypedGraph<SG,I,RV,RVT,RE,RET>,
    // rel
    R extends TypedEdge<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G>,
    RT extends TypedEdge.Type<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G>,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // tgt
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>
  >
  Stream<S> inV(RT relType, N node) {

    return raw().inV(
      node.raw(),
      relType.raw()
    ).map(
      relType.sourceType()::vertex
    );
  }

  default <
    // src
    S extends TypedVertex<S,ST,SG,I,RV,RVT,RE,RET>,
    ST extends TypedVertex.Type<S,ST,SG,I,RV,RVT,RE,RET>,
    SG extends TypedGraph<SG,I,RV,RVT,RE,RET>,
    // rel
    R extends TypedEdge<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G>,
    RT extends TypedEdge.Type<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G> &
      TypedEdge.Type.FromOne,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // tgt
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>
  >
  R inOneE(RT relType, N node) {

    return relType.edge(
      raw().inE(
        node.raw(),
        relType.raw()
      )
      .findFirst().get()
    );
  }
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,RV,RVT,RE,RET>,
    ST extends TypedVertex.Type<S,ST,SG,I,RV,RVT,RE,RET>,
    SG extends TypedGraph<SG,I,RV,RVT,RE,RET>,
    // rel
    R extends TypedEdge<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G>,
    RT extends TypedEdge.Type<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G> &
      TypedEdge.Type.FromOne,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // tgt
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>
  >
  S inOneV(RT relType, N node) {

    return relType.sourceType().vertex(
      raw().inV(
        node.raw(),
        relType.raw()
      )
      .findFirst().get()
    );
  }

  default <
    // src
    S extends TypedVertex<S,ST,SG,I,RV,RVT,RE,RET>,
    ST extends TypedVertex.Type<S,ST,SG,I,RV,RVT,RE,RET>,
    SG extends TypedGraph<SG,I,RV,RVT,RE,RET>,
    // rel
    R extends TypedEdge<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G>,
    RT extends TypedEdge.Type<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G> &
      TypedEdge.Type.FromAtMostOne,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // tgt
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>
  >
  Optional<R> inOptionalE(RT relType, N node) {

    return raw().inE(
      node.raw(),
      relType.raw()
    )
    .findFirst()
    .map(
      relType::edge
    );
  }
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,RV,RVT,RE,RET>,
    ST extends TypedVertex.Type<S,ST,SG,I,RV,RVT,RE,RET>,
    SG extends TypedGraph<SG,I,RV,RVT,RE,RET>,
    // rel
    R extends TypedEdge<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G>,
    RT extends TypedEdge.Type<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G> &
      TypedEdge.Type.FromAtMostOne,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // tgt
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>
  >
  Optional<S> inOptionalV(RT relType, N node) {

    return raw().inV(
      node.raw(),
      relType.raw()
    )
    .findFirst()
    .map(
      relType.sourceType()::vertex
    );
  }

  default <
    // src
    S extends TypedVertex<S,ST,SG,I,RV,RVT,RE,RET>,
    ST extends TypedVertex.Type<S,ST,SG,I,RV,RVT,RE,RET>,
    SG extends TypedGraph<SG,I,RV,RVT,RE,RET>,
    // rel
    R extends TypedEdge<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G>,
    RT extends TypedEdge.Type<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G> &
      TypedEdge.Type.FromAtLeastOne,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // tgt
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>
  >
  Stream<R> inManyE(RT relType, N node) {

    return raw().inE(
      node.raw(),
      relType.raw()
    ).map(
      relType::edge
    );
  }
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,RV,RVT,RE,RET>,
    ST extends TypedVertex.Type<S,ST,SG,I,RV,RVT,RE,RET>,
    SG extends TypedGraph<SG,I,RV,RVT,RE,RET>,
    // rel
    R extends TypedEdge<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G>,
    RT extends TypedEdge.Type<S,ST,SG, R,RT,RG,I,RV,RVT,RE,RET, N,NT,G> &
      TypedEdge.Type.FromAtLeastOne,
    RG extends TypedGraph<RG,I,RV,RVT,RE,RET>,
    // tgt
    N extends TypedVertex<N,NT,G,I,RV,RVT,RE,RET>,
    NT extends TypedVertex.Type<N,NT,G,I,RV,RVT,RE,RET>
  >
  Stream<S> inManyV(RT relType, N node) {

    return raw().inV(
      node.raw(),
      relType.raw()
    ).map(
      relType.sourceType()::vertex
    );
  }
}
