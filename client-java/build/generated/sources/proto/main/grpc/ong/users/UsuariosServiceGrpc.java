package ong.users;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class UsuariosServiceGrpc {

  private UsuariosServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "ong.users.UsuariosService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ong.users.Users.CreateUserRequest,
      ong.users.Users.CreateUserResponse> getCreateUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateUser",
      requestType = ong.users.Users.CreateUserRequest.class,
      responseType = ong.users.Users.CreateUserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ong.users.Users.CreateUserRequest,
      ong.users.Users.CreateUserResponse> getCreateUserMethod() {
    io.grpc.MethodDescriptor<ong.users.Users.CreateUserRequest, ong.users.Users.CreateUserResponse> getCreateUserMethod;
    if ((getCreateUserMethod = UsuariosServiceGrpc.getCreateUserMethod) == null) {
      synchronized (UsuariosServiceGrpc.class) {
        if ((getCreateUserMethod = UsuariosServiceGrpc.getCreateUserMethod) == null) {
          UsuariosServiceGrpc.getCreateUserMethod = getCreateUserMethod =
              io.grpc.MethodDescriptor.<ong.users.Users.CreateUserRequest, ong.users.Users.CreateUserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.users.Users.CreateUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.users.Users.CreateUserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UsuariosServiceMethodDescriptorSupplier("CreateUser"))
              .build();
        }
      }
    }
    return getCreateUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ong.users.Users.UpdateUserRequest,
      ong.users.Users.User> getUpdateUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateUser",
      requestType = ong.users.Users.UpdateUserRequest.class,
      responseType = ong.users.Users.User.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ong.users.Users.UpdateUserRequest,
      ong.users.Users.User> getUpdateUserMethod() {
    io.grpc.MethodDescriptor<ong.users.Users.UpdateUserRequest, ong.users.Users.User> getUpdateUserMethod;
    if ((getUpdateUserMethod = UsuariosServiceGrpc.getUpdateUserMethod) == null) {
      synchronized (UsuariosServiceGrpc.class) {
        if ((getUpdateUserMethod = UsuariosServiceGrpc.getUpdateUserMethod) == null) {
          UsuariosServiceGrpc.getUpdateUserMethod = getUpdateUserMethod =
              io.grpc.MethodDescriptor.<ong.users.Users.UpdateUserRequest, ong.users.Users.User>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.users.Users.UpdateUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.users.Users.User.getDefaultInstance()))
              .setSchemaDescriptor(new UsuariosServiceMethodDescriptorSupplier("UpdateUser"))
              .build();
        }
      }
    }
    return getUpdateUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ong.users.Users.UserIdRequest,
      ong.users.Users.User> getDeactivateUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeactivateUser",
      requestType = ong.users.Users.UserIdRequest.class,
      responseType = ong.users.Users.User.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ong.users.Users.UserIdRequest,
      ong.users.Users.User> getDeactivateUserMethod() {
    io.grpc.MethodDescriptor<ong.users.Users.UserIdRequest, ong.users.Users.User> getDeactivateUserMethod;
    if ((getDeactivateUserMethod = UsuariosServiceGrpc.getDeactivateUserMethod) == null) {
      synchronized (UsuariosServiceGrpc.class) {
        if ((getDeactivateUserMethod = UsuariosServiceGrpc.getDeactivateUserMethod) == null) {
          UsuariosServiceGrpc.getDeactivateUserMethod = getDeactivateUserMethod =
              io.grpc.MethodDescriptor.<ong.users.Users.UserIdRequest, ong.users.Users.User>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeactivateUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.users.Users.UserIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.users.Users.User.getDefaultInstance()))
              .setSchemaDescriptor(new UsuariosServiceMethodDescriptorSupplier("DeactivateUser"))
              .build();
        }
      }
    }
    return getDeactivateUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ong.users.Users.UserList> getListUsersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListUsers",
      requestType = com.google.protobuf.Empty.class,
      responseType = ong.users.Users.UserList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ong.users.Users.UserList> getListUsersMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, ong.users.Users.UserList> getListUsersMethod;
    if ((getListUsersMethod = UsuariosServiceGrpc.getListUsersMethod) == null) {
      synchronized (UsuariosServiceGrpc.class) {
        if ((getListUsersMethod = UsuariosServiceGrpc.getListUsersMethod) == null) {
          UsuariosServiceGrpc.getListUsersMethod = getListUsersMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, ong.users.Users.UserList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListUsers"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.users.Users.UserList.getDefaultInstance()))
              .setSchemaDescriptor(new UsuariosServiceMethodDescriptorSupplier("ListUsers"))
              .build();
        }
      }
    }
    return getListUsersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ong.users.Users.LoginRequest,
      ong.users.Users.LoginResponse> getLoginMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Login",
      requestType = ong.users.Users.LoginRequest.class,
      responseType = ong.users.Users.LoginResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ong.users.Users.LoginRequest,
      ong.users.Users.LoginResponse> getLoginMethod() {
    io.grpc.MethodDescriptor<ong.users.Users.LoginRequest, ong.users.Users.LoginResponse> getLoginMethod;
    if ((getLoginMethod = UsuariosServiceGrpc.getLoginMethod) == null) {
      synchronized (UsuariosServiceGrpc.class) {
        if ((getLoginMethod = UsuariosServiceGrpc.getLoginMethod) == null) {
          UsuariosServiceGrpc.getLoginMethod = getLoginMethod =
              io.grpc.MethodDescriptor.<ong.users.Users.LoginRequest, ong.users.Users.LoginResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Login"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.users.Users.LoginRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.users.Users.LoginResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UsuariosServiceMethodDescriptorSupplier("Login"))
              .build();
        }
      }
    }
    return getLoginMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ong.users.Users.GetUserRequest,
      ong.users.Users.GetUserResponse> getGetUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUser",
      requestType = ong.users.Users.GetUserRequest.class,
      responseType = ong.users.Users.GetUserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ong.users.Users.GetUserRequest,
      ong.users.Users.GetUserResponse> getGetUserMethod() {
    io.grpc.MethodDescriptor<ong.users.Users.GetUserRequest, ong.users.Users.GetUserResponse> getGetUserMethod;
    if ((getGetUserMethod = UsuariosServiceGrpc.getGetUserMethod) == null) {
      synchronized (UsuariosServiceGrpc.class) {
        if ((getGetUserMethod = UsuariosServiceGrpc.getGetUserMethod) == null) {
          UsuariosServiceGrpc.getGetUserMethod = getGetUserMethod =
              io.grpc.MethodDescriptor.<ong.users.Users.GetUserRequest, ong.users.Users.GetUserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.users.Users.GetUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.users.Users.GetUserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UsuariosServiceMethodDescriptorSupplier("GetUser"))
              .build();
        }
      }
    }
    return getGetUserMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UsuariosServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UsuariosServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UsuariosServiceStub>() {
        @java.lang.Override
        public UsuariosServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UsuariosServiceStub(channel, callOptions);
        }
      };
    return UsuariosServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static UsuariosServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UsuariosServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UsuariosServiceBlockingV2Stub>() {
        @java.lang.Override
        public UsuariosServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UsuariosServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return UsuariosServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UsuariosServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UsuariosServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UsuariosServiceBlockingStub>() {
        @java.lang.Override
        public UsuariosServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UsuariosServiceBlockingStub(channel, callOptions);
        }
      };
    return UsuariosServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UsuariosServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UsuariosServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UsuariosServiceFutureStub>() {
        @java.lang.Override
        public UsuariosServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UsuariosServiceFutureStub(channel, callOptions);
        }
      };
    return UsuariosServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createUser(ong.users.Users.CreateUserRequest request,
        io.grpc.stub.StreamObserver<ong.users.Users.CreateUserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateUserMethod(), responseObserver);
    }

    /**
     */
    default void updateUser(ong.users.Users.UpdateUserRequest request,
        io.grpc.stub.StreamObserver<ong.users.Users.User> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateUserMethod(), responseObserver);
    }

    /**
     */
    default void deactivateUser(ong.users.Users.UserIdRequest request,
        io.grpc.stub.StreamObserver<ong.users.Users.User> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeactivateUserMethod(), responseObserver);
    }

    /**
     */
    default void listUsers(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ong.users.Users.UserList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListUsersMethod(), responseObserver);
    }

    /**
     */
    default void login(ong.users.Users.LoginRequest request,
        io.grpc.stub.StreamObserver<ong.users.Users.LoginResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLoginMethod(), responseObserver);
    }

    /**
     */
    default void getUser(ong.users.Users.GetUserRequest request,
        io.grpc.stub.StreamObserver<ong.users.Users.GetUserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUserMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service UsuariosService.
   */
  public static abstract class UsuariosServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return UsuariosServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service UsuariosService.
   */
  public static final class UsuariosServiceStub
      extends io.grpc.stub.AbstractAsyncStub<UsuariosServiceStub> {
    private UsuariosServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UsuariosServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UsuariosServiceStub(channel, callOptions);
    }

    /**
     */
    public void createUser(ong.users.Users.CreateUserRequest request,
        io.grpc.stub.StreamObserver<ong.users.Users.CreateUserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateUser(ong.users.Users.UpdateUserRequest request,
        io.grpc.stub.StreamObserver<ong.users.Users.User> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deactivateUser(ong.users.Users.UserIdRequest request,
        io.grpc.stub.StreamObserver<ong.users.Users.User> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeactivateUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listUsers(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ong.users.Users.UserList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListUsersMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void login(ong.users.Users.LoginRequest request,
        io.grpc.stub.StreamObserver<ong.users.Users.LoginResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getUser(ong.users.Users.GetUserRequest request,
        io.grpc.stub.StreamObserver<ong.users.Users.GetUserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUserMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service UsuariosService.
   */
  public static final class UsuariosServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<UsuariosServiceBlockingV2Stub> {
    private UsuariosServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UsuariosServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UsuariosServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public ong.users.Users.CreateUserResponse createUser(ong.users.Users.CreateUserRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCreateUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.users.Users.User updateUser(ong.users.Users.UpdateUserRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getUpdateUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.users.Users.User deactivateUser(ong.users.Users.UserIdRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getDeactivateUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.users.Users.UserList listUsers(com.google.protobuf.Empty request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getListUsersMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.users.Users.LoginResponse login(ong.users.Users.LoginRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getLoginMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.users.Users.GetUserResponse getUser(ong.users.Users.GetUserRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetUserMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service UsuariosService.
   */
  public static final class UsuariosServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<UsuariosServiceBlockingStub> {
    private UsuariosServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UsuariosServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UsuariosServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public ong.users.Users.CreateUserResponse createUser(ong.users.Users.CreateUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.users.Users.User updateUser(ong.users.Users.UpdateUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.users.Users.User deactivateUser(ong.users.Users.UserIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeactivateUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.users.Users.UserList listUsers(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListUsersMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.users.Users.LoginResponse login(ong.users.Users.LoginRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLoginMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.users.Users.GetUserResponse getUser(ong.users.Users.GetUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUserMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service UsuariosService.
   */
  public static final class UsuariosServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<UsuariosServiceFutureStub> {
    private UsuariosServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UsuariosServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UsuariosServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.users.Users.CreateUserResponse> createUser(
        ong.users.Users.CreateUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.users.Users.User> updateUser(
        ong.users.Users.UpdateUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.users.Users.User> deactivateUser(
        ong.users.Users.UserIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeactivateUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.users.Users.UserList> listUsers(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListUsersMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.users.Users.LoginResponse> login(
        ong.users.Users.LoginRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.users.Users.GetUserResponse> getUser(
        ong.users.Users.GetUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUserMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_USER = 0;
  private static final int METHODID_UPDATE_USER = 1;
  private static final int METHODID_DEACTIVATE_USER = 2;
  private static final int METHODID_LIST_USERS = 3;
  private static final int METHODID_LOGIN = 4;
  private static final int METHODID_GET_USER = 5;

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
        case METHODID_CREATE_USER:
          serviceImpl.createUser((ong.users.Users.CreateUserRequest) request,
              (io.grpc.stub.StreamObserver<ong.users.Users.CreateUserResponse>) responseObserver);
          break;
        case METHODID_UPDATE_USER:
          serviceImpl.updateUser((ong.users.Users.UpdateUserRequest) request,
              (io.grpc.stub.StreamObserver<ong.users.Users.User>) responseObserver);
          break;
        case METHODID_DEACTIVATE_USER:
          serviceImpl.deactivateUser((ong.users.Users.UserIdRequest) request,
              (io.grpc.stub.StreamObserver<ong.users.Users.User>) responseObserver);
          break;
        case METHODID_LIST_USERS:
          serviceImpl.listUsers((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<ong.users.Users.UserList>) responseObserver);
          break;
        case METHODID_LOGIN:
          serviceImpl.login((ong.users.Users.LoginRequest) request,
              (io.grpc.stub.StreamObserver<ong.users.Users.LoginResponse>) responseObserver);
          break;
        case METHODID_GET_USER:
          serviceImpl.getUser((ong.users.Users.GetUserRequest) request,
              (io.grpc.stub.StreamObserver<ong.users.Users.GetUserResponse>) responseObserver);
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
          getCreateUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ong.users.Users.CreateUserRequest,
              ong.users.Users.CreateUserResponse>(
                service, METHODID_CREATE_USER)))
        .addMethod(
          getUpdateUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ong.users.Users.UpdateUserRequest,
              ong.users.Users.User>(
                service, METHODID_UPDATE_USER)))
        .addMethod(
          getDeactivateUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ong.users.Users.UserIdRequest,
              ong.users.Users.User>(
                service, METHODID_DEACTIVATE_USER)))
        .addMethod(
          getListUsersMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              ong.users.Users.UserList>(
                service, METHODID_LIST_USERS)))
        .addMethod(
          getLoginMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ong.users.Users.LoginRequest,
              ong.users.Users.LoginResponse>(
                service, METHODID_LOGIN)))
        .addMethod(
          getGetUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ong.users.Users.GetUserRequest,
              ong.users.Users.GetUserResponse>(
                service, METHODID_GET_USER)))
        .build();
  }

  private static abstract class UsuariosServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UsuariosServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ong.users.Users.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UsuariosService");
    }
  }

  private static final class UsuariosServiceFileDescriptorSupplier
      extends UsuariosServiceBaseDescriptorSupplier {
    UsuariosServiceFileDescriptorSupplier() {}
  }

  private static final class UsuariosServiceMethodDescriptorSupplier
      extends UsuariosServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    UsuariosServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (UsuariosServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UsuariosServiceFileDescriptorSupplier())
              .addMethod(getCreateUserMethod())
              .addMethod(getUpdateUserMethod())
              .addMethod(getDeactivateUserMethod())
              .addMethod(getListUsersMethod())
              .addMethod(getLoginMethod())
              .addMethod(getGetUserMethod())
              .build();
        }
      }
    }
    return result;
  }
}
