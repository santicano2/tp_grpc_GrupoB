package ong.events;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class EventosServiceGrpc {

  private EventosServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "ong.events.EventosService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ong.events.Events.CreateEventRequest,
      ong.events.Events.Event> getCreateEventMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateEvent",
      requestType = ong.events.Events.CreateEventRequest.class,
      responseType = ong.events.Events.Event.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ong.events.Events.CreateEventRequest,
      ong.events.Events.Event> getCreateEventMethod() {
    io.grpc.MethodDescriptor<ong.events.Events.CreateEventRequest, ong.events.Events.Event> getCreateEventMethod;
    if ((getCreateEventMethod = EventosServiceGrpc.getCreateEventMethod) == null) {
      synchronized (EventosServiceGrpc.class) {
        if ((getCreateEventMethod = EventosServiceGrpc.getCreateEventMethod) == null) {
          EventosServiceGrpc.getCreateEventMethod = getCreateEventMethod =
              io.grpc.MethodDescriptor.<ong.events.Events.CreateEventRequest, ong.events.Events.Event>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateEvent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.events.Events.CreateEventRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.events.Events.Event.getDefaultInstance()))
              .setSchemaDescriptor(new EventosServiceMethodDescriptorSupplier("CreateEvent"))
              .build();
        }
      }
    }
    return getCreateEventMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ong.events.Events.UpdateEventRequest,
      ong.events.Events.Event> getUpdateEventMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateEvent",
      requestType = ong.events.Events.UpdateEventRequest.class,
      responseType = ong.events.Events.Event.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ong.events.Events.UpdateEventRequest,
      ong.events.Events.Event> getUpdateEventMethod() {
    io.grpc.MethodDescriptor<ong.events.Events.UpdateEventRequest, ong.events.Events.Event> getUpdateEventMethod;
    if ((getUpdateEventMethod = EventosServiceGrpc.getUpdateEventMethod) == null) {
      synchronized (EventosServiceGrpc.class) {
        if ((getUpdateEventMethod = EventosServiceGrpc.getUpdateEventMethod) == null) {
          EventosServiceGrpc.getUpdateEventMethod = getUpdateEventMethod =
              io.grpc.MethodDescriptor.<ong.events.Events.UpdateEventRequest, ong.events.Events.Event>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateEvent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.events.Events.UpdateEventRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.events.Events.Event.getDefaultInstance()))
              .setSchemaDescriptor(new EventosServiceMethodDescriptorSupplier("UpdateEvent"))
              .build();
        }
      }
    }
    return getUpdateEventMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ong.events.Events.EventIdRequest,
      com.google.protobuf.Empty> getDeleteFutureEventMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteFutureEvent",
      requestType = ong.events.Events.EventIdRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ong.events.Events.EventIdRequest,
      com.google.protobuf.Empty> getDeleteFutureEventMethod() {
    io.grpc.MethodDescriptor<ong.events.Events.EventIdRequest, com.google.protobuf.Empty> getDeleteFutureEventMethod;
    if ((getDeleteFutureEventMethod = EventosServiceGrpc.getDeleteFutureEventMethod) == null) {
      synchronized (EventosServiceGrpc.class) {
        if ((getDeleteFutureEventMethod = EventosServiceGrpc.getDeleteFutureEventMethod) == null) {
          EventosServiceGrpc.getDeleteFutureEventMethod = getDeleteFutureEventMethod =
              io.grpc.MethodDescriptor.<ong.events.Events.EventIdRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteFutureEvent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.events.Events.EventIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new EventosServiceMethodDescriptorSupplier("DeleteFutureEvent"))
              .build();
        }
      }
    }
    return getDeleteFutureEventMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ong.events.Events.MemberChangeRequest,
      ong.events.Events.Event> getAssignOrRemoveMemberMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AssignOrRemoveMember",
      requestType = ong.events.Events.MemberChangeRequest.class,
      responseType = ong.events.Events.Event.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ong.events.Events.MemberChangeRequest,
      ong.events.Events.Event> getAssignOrRemoveMemberMethod() {
    io.grpc.MethodDescriptor<ong.events.Events.MemberChangeRequest, ong.events.Events.Event> getAssignOrRemoveMemberMethod;
    if ((getAssignOrRemoveMemberMethod = EventosServiceGrpc.getAssignOrRemoveMemberMethod) == null) {
      synchronized (EventosServiceGrpc.class) {
        if ((getAssignOrRemoveMemberMethod = EventosServiceGrpc.getAssignOrRemoveMemberMethod) == null) {
          EventosServiceGrpc.getAssignOrRemoveMemberMethod = getAssignOrRemoveMemberMethod =
              io.grpc.MethodDescriptor.<ong.events.Events.MemberChangeRequest, ong.events.Events.Event>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AssignOrRemoveMember"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.events.Events.MemberChangeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.events.Events.Event.getDefaultInstance()))
              .setSchemaDescriptor(new EventosServiceMethodDescriptorSupplier("AssignOrRemoveMember"))
              .build();
        }
      }
    }
    return getAssignOrRemoveMemberMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ong.events.Events.RegisterDistributionRequest,
      ong.events.Events.Event> getRegisterDistributionsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterDistributions",
      requestType = ong.events.Events.RegisterDistributionRequest.class,
      responseType = ong.events.Events.Event.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ong.events.Events.RegisterDistributionRequest,
      ong.events.Events.Event> getRegisterDistributionsMethod() {
    io.grpc.MethodDescriptor<ong.events.Events.RegisterDistributionRequest, ong.events.Events.Event> getRegisterDistributionsMethod;
    if ((getRegisterDistributionsMethod = EventosServiceGrpc.getRegisterDistributionsMethod) == null) {
      synchronized (EventosServiceGrpc.class) {
        if ((getRegisterDistributionsMethod = EventosServiceGrpc.getRegisterDistributionsMethod) == null) {
          EventosServiceGrpc.getRegisterDistributionsMethod = getRegisterDistributionsMethod =
              io.grpc.MethodDescriptor.<ong.events.Events.RegisterDistributionRequest, ong.events.Events.Event>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegisterDistributions"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.events.Events.RegisterDistributionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.events.Events.Event.getDefaultInstance()))
              .setSchemaDescriptor(new EventosServiceMethodDescriptorSupplier("RegisterDistributions"))
              .build();
        }
      }
    }
    return getRegisterDistributionsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ong.events.Events.EventList> getListEventsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListEvents",
      requestType = com.google.protobuf.Empty.class,
      responseType = ong.events.Events.EventList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ong.events.Events.EventList> getListEventsMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, ong.events.Events.EventList> getListEventsMethod;
    if ((getListEventsMethod = EventosServiceGrpc.getListEventsMethod) == null) {
      synchronized (EventosServiceGrpc.class) {
        if ((getListEventsMethod = EventosServiceGrpc.getListEventsMethod) == null) {
          EventosServiceGrpc.getListEventsMethod = getListEventsMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, ong.events.Events.EventList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListEvents"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.events.Events.EventList.getDefaultInstance()))
              .setSchemaDescriptor(new EventosServiceMethodDescriptorSupplier("ListEvents"))
              .build();
        }
      }
    }
    return getListEventsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static EventosServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventosServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventosServiceStub>() {
        @java.lang.Override
        public EventosServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventosServiceStub(channel, callOptions);
        }
      };
    return EventosServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static EventosServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventosServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventosServiceBlockingV2Stub>() {
        @java.lang.Override
        public EventosServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventosServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return EventosServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static EventosServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventosServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventosServiceBlockingStub>() {
        @java.lang.Override
        public EventosServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventosServiceBlockingStub(channel, callOptions);
        }
      };
    return EventosServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static EventosServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventosServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventosServiceFutureStub>() {
        @java.lang.Override
        public EventosServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventosServiceFutureStub(channel, callOptions);
        }
      };
    return EventosServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createEvent(ong.events.Events.CreateEventRequest request,
        io.grpc.stub.StreamObserver<ong.events.Events.Event> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateEventMethod(), responseObserver);
    }

    /**
     */
    default void updateEvent(ong.events.Events.UpdateEventRequest request,
        io.grpc.stub.StreamObserver<ong.events.Events.Event> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateEventMethod(), responseObserver);
    }

    /**
     */
    default void deleteFutureEvent(ong.events.Events.EventIdRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteFutureEventMethod(), responseObserver);
    }

    /**
     */
    default void assignOrRemoveMember(ong.events.Events.MemberChangeRequest request,
        io.grpc.stub.StreamObserver<ong.events.Events.Event> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAssignOrRemoveMemberMethod(), responseObserver);
    }

    /**
     */
    default void registerDistributions(ong.events.Events.RegisterDistributionRequest request,
        io.grpc.stub.StreamObserver<ong.events.Events.Event> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterDistributionsMethod(), responseObserver);
    }

    /**
     */
    default void listEvents(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ong.events.Events.EventList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListEventsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service EventosService.
   */
  public static abstract class EventosServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return EventosServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service EventosService.
   */
  public static final class EventosServiceStub
      extends io.grpc.stub.AbstractAsyncStub<EventosServiceStub> {
    private EventosServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventosServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventosServiceStub(channel, callOptions);
    }

    /**
     */
    public void createEvent(ong.events.Events.CreateEventRequest request,
        io.grpc.stub.StreamObserver<ong.events.Events.Event> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateEventMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateEvent(ong.events.Events.UpdateEventRequest request,
        io.grpc.stub.StreamObserver<ong.events.Events.Event> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateEventMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteFutureEvent(ong.events.Events.EventIdRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteFutureEventMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void assignOrRemoveMember(ong.events.Events.MemberChangeRequest request,
        io.grpc.stub.StreamObserver<ong.events.Events.Event> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAssignOrRemoveMemberMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registerDistributions(ong.events.Events.RegisterDistributionRequest request,
        io.grpc.stub.StreamObserver<ong.events.Events.Event> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterDistributionsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listEvents(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ong.events.Events.EventList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListEventsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service EventosService.
   */
  public static final class EventosServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<EventosServiceBlockingV2Stub> {
    private EventosServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventosServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventosServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public ong.events.Events.Event createEvent(ong.events.Events.CreateEventRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCreateEventMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.events.Events.Event updateEvent(ong.events.Events.UpdateEventRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getUpdateEventMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty deleteFutureEvent(ong.events.Events.EventIdRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getDeleteFutureEventMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.events.Events.Event assignOrRemoveMember(ong.events.Events.MemberChangeRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getAssignOrRemoveMemberMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.events.Events.Event registerDistributions(ong.events.Events.RegisterDistributionRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getRegisterDistributionsMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.events.Events.EventList listEvents(com.google.protobuf.Empty request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getListEventsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service EventosService.
   */
  public static final class EventosServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<EventosServiceBlockingStub> {
    private EventosServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventosServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventosServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public ong.events.Events.Event createEvent(ong.events.Events.CreateEventRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateEventMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.events.Events.Event updateEvent(ong.events.Events.UpdateEventRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateEventMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty deleteFutureEvent(ong.events.Events.EventIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteFutureEventMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.events.Events.Event assignOrRemoveMember(ong.events.Events.MemberChangeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAssignOrRemoveMemberMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.events.Events.Event registerDistributions(ong.events.Events.RegisterDistributionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterDistributionsMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.events.Events.EventList listEvents(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListEventsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service EventosService.
   */
  public static final class EventosServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<EventosServiceFutureStub> {
    private EventosServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventosServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventosServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.events.Events.Event> createEvent(
        ong.events.Events.CreateEventRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateEventMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.events.Events.Event> updateEvent(
        ong.events.Events.UpdateEventRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateEventMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> deleteFutureEvent(
        ong.events.Events.EventIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteFutureEventMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.events.Events.Event> assignOrRemoveMember(
        ong.events.Events.MemberChangeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAssignOrRemoveMemberMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.events.Events.Event> registerDistributions(
        ong.events.Events.RegisterDistributionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterDistributionsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.events.Events.EventList> listEvents(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListEventsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_EVENT = 0;
  private static final int METHODID_UPDATE_EVENT = 1;
  private static final int METHODID_DELETE_FUTURE_EVENT = 2;
  private static final int METHODID_ASSIGN_OR_REMOVE_MEMBER = 3;
  private static final int METHODID_REGISTER_DISTRIBUTIONS = 4;
  private static final int METHODID_LIST_EVENTS = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_EVENT:
          serviceImpl.createEvent((ong.events.Events.CreateEventRequest) request,
              (io.grpc.stub.StreamObserver<ong.events.Events.Event>) responseObserver);
          break;
        case METHODID_UPDATE_EVENT:
          serviceImpl.updateEvent((ong.events.Events.UpdateEventRequest) request,
              (io.grpc.stub.StreamObserver<ong.events.Events.Event>) responseObserver);
          break;
        case METHODID_DELETE_FUTURE_EVENT:
          serviceImpl.deleteFutureEvent((ong.events.Events.EventIdRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_ASSIGN_OR_REMOVE_MEMBER:
          serviceImpl.assignOrRemoveMember((ong.events.Events.MemberChangeRequest) request,
              (io.grpc.stub.StreamObserver<ong.events.Events.Event>) responseObserver);
          break;
        case METHODID_REGISTER_DISTRIBUTIONS:
          serviceImpl.registerDistributions((ong.events.Events.RegisterDistributionRequest) request,
              (io.grpc.stub.StreamObserver<ong.events.Events.Event>) responseObserver);
          break;
        case METHODID_LIST_EVENTS:
          serviceImpl.listEvents((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<ong.events.Events.EventList>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getCreateEventMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ong.events.Events.CreateEventRequest,
              ong.events.Events.Event>(
                service, METHODID_CREATE_EVENT)))
        .addMethod(
          getUpdateEventMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ong.events.Events.UpdateEventRequest,
              ong.events.Events.Event>(
                service, METHODID_UPDATE_EVENT)))
        .addMethod(
          getDeleteFutureEventMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ong.events.Events.EventIdRequest,
              com.google.protobuf.Empty>(
                service, METHODID_DELETE_FUTURE_EVENT)))
        .addMethod(
          getAssignOrRemoveMemberMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ong.events.Events.MemberChangeRequest,
              ong.events.Events.Event>(
                service, METHODID_ASSIGN_OR_REMOVE_MEMBER)))
        .addMethod(
          getRegisterDistributionsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ong.events.Events.RegisterDistributionRequest,
              ong.events.Events.Event>(
                service, METHODID_REGISTER_DISTRIBUTIONS)))
        .addMethod(
          getListEventsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              ong.events.Events.EventList>(
                service, METHODID_LIST_EVENTS)))
        .build();
  }

  private static abstract class EventosServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    EventosServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ong.events.Events.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("EventosService");
    }
  }

  private static final class EventosServiceFileDescriptorSupplier
      extends EventosServiceBaseDescriptorSupplier {
    EventosServiceFileDescriptorSupplier() {}
  }

  private static final class EventosServiceMethodDescriptorSupplier
      extends EventosServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    EventosServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (EventosServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new EventosServiceFileDescriptorSupplier())
              .addMethod(getCreateEventMethod())
              .addMethod(getUpdateEventMethod())
              .addMethod(getDeleteFutureEventMethod())
              .addMethod(getAssignOrRemoveMemberMethod())
              .addMethod(getRegisterDistributionsMethod())
              .addMethod(getListEventsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
