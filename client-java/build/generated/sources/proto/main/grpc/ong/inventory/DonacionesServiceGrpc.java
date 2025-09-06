package ong.inventory;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class DonacionesServiceGrpc {

  private DonacionesServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "ong.inventory.DonacionesService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ong.inventory.Inventory.CreateDonationRequest,
      ong.inventory.Inventory.DonationItem> getCreateDonationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateDonation",
      requestType = ong.inventory.Inventory.CreateDonationRequest.class,
      responseType = ong.inventory.Inventory.DonationItem.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ong.inventory.Inventory.CreateDonationRequest,
      ong.inventory.Inventory.DonationItem> getCreateDonationMethod() {
    io.grpc.MethodDescriptor<ong.inventory.Inventory.CreateDonationRequest, ong.inventory.Inventory.DonationItem> getCreateDonationMethod;
    if ((getCreateDonationMethod = DonacionesServiceGrpc.getCreateDonationMethod) == null) {
      synchronized (DonacionesServiceGrpc.class) {
        if ((getCreateDonationMethod = DonacionesServiceGrpc.getCreateDonationMethod) == null) {
          DonacionesServiceGrpc.getCreateDonationMethod = getCreateDonationMethod =
              io.grpc.MethodDescriptor.<ong.inventory.Inventory.CreateDonationRequest, ong.inventory.Inventory.DonationItem>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateDonation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.inventory.Inventory.CreateDonationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.inventory.Inventory.DonationItem.getDefaultInstance()))
              .setSchemaDescriptor(new DonacionesServiceMethodDescriptorSupplier("CreateDonation"))
              .build();
        }
      }
    }
    return getCreateDonationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ong.inventory.Inventory.UpdateDonationRequest,
      ong.inventory.Inventory.DonationItem> getUpdateDonationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateDonation",
      requestType = ong.inventory.Inventory.UpdateDonationRequest.class,
      responseType = ong.inventory.Inventory.DonationItem.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ong.inventory.Inventory.UpdateDonationRequest,
      ong.inventory.Inventory.DonationItem> getUpdateDonationMethod() {
    io.grpc.MethodDescriptor<ong.inventory.Inventory.UpdateDonationRequest, ong.inventory.Inventory.DonationItem> getUpdateDonationMethod;
    if ((getUpdateDonationMethod = DonacionesServiceGrpc.getUpdateDonationMethod) == null) {
      synchronized (DonacionesServiceGrpc.class) {
        if ((getUpdateDonationMethod = DonacionesServiceGrpc.getUpdateDonationMethod) == null) {
          DonacionesServiceGrpc.getUpdateDonationMethod = getUpdateDonationMethod =
              io.grpc.MethodDescriptor.<ong.inventory.Inventory.UpdateDonationRequest, ong.inventory.Inventory.DonationItem>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateDonation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.inventory.Inventory.UpdateDonationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.inventory.Inventory.DonationItem.getDefaultInstance()))
              .setSchemaDescriptor(new DonacionesServiceMethodDescriptorSupplier("UpdateDonation"))
              .build();
        }
      }
    }
    return getUpdateDonationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ong.inventory.Inventory.DonationIdRequest,
      ong.inventory.Inventory.DonationItem> getDeleteDonationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteDonation",
      requestType = ong.inventory.Inventory.DonationIdRequest.class,
      responseType = ong.inventory.Inventory.DonationItem.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ong.inventory.Inventory.DonationIdRequest,
      ong.inventory.Inventory.DonationItem> getDeleteDonationMethod() {
    io.grpc.MethodDescriptor<ong.inventory.Inventory.DonationIdRequest, ong.inventory.Inventory.DonationItem> getDeleteDonationMethod;
    if ((getDeleteDonationMethod = DonacionesServiceGrpc.getDeleteDonationMethod) == null) {
      synchronized (DonacionesServiceGrpc.class) {
        if ((getDeleteDonationMethod = DonacionesServiceGrpc.getDeleteDonationMethod) == null) {
          DonacionesServiceGrpc.getDeleteDonationMethod = getDeleteDonationMethod =
              io.grpc.MethodDescriptor.<ong.inventory.Inventory.DonationIdRequest, ong.inventory.Inventory.DonationItem>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteDonation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.inventory.Inventory.DonationIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.inventory.Inventory.DonationItem.getDefaultInstance()))
              .setSchemaDescriptor(new DonacionesServiceMethodDescriptorSupplier("DeleteDonation"))
              .build();
        }
      }
    }
    return getDeleteDonationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ong.inventory.Inventory.DonationList> getListDonationsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListDonations",
      requestType = com.google.protobuf.Empty.class,
      responseType = ong.inventory.Inventory.DonationList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ong.inventory.Inventory.DonationList> getListDonationsMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, ong.inventory.Inventory.DonationList> getListDonationsMethod;
    if ((getListDonationsMethod = DonacionesServiceGrpc.getListDonationsMethod) == null) {
      synchronized (DonacionesServiceGrpc.class) {
        if ((getListDonationsMethod = DonacionesServiceGrpc.getListDonationsMethod) == null) {
          DonacionesServiceGrpc.getListDonationsMethod = getListDonationsMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, ong.inventory.Inventory.DonationList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListDonations"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ong.inventory.Inventory.DonationList.getDefaultInstance()))
              .setSchemaDescriptor(new DonacionesServiceMethodDescriptorSupplier("ListDonations"))
              .build();
        }
      }
    }
    return getListDonationsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DonacionesServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DonacionesServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DonacionesServiceStub>() {
        @java.lang.Override
        public DonacionesServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DonacionesServiceStub(channel, callOptions);
        }
      };
    return DonacionesServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static DonacionesServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DonacionesServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DonacionesServiceBlockingV2Stub>() {
        @java.lang.Override
        public DonacionesServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DonacionesServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return DonacionesServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DonacionesServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DonacionesServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DonacionesServiceBlockingStub>() {
        @java.lang.Override
        public DonacionesServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DonacionesServiceBlockingStub(channel, callOptions);
        }
      };
    return DonacionesServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DonacionesServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DonacionesServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DonacionesServiceFutureStub>() {
        @java.lang.Override
        public DonacionesServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DonacionesServiceFutureStub(channel, callOptions);
        }
      };
    return DonacionesServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createDonation(ong.inventory.Inventory.CreateDonationRequest request,
        io.grpc.stub.StreamObserver<ong.inventory.Inventory.DonationItem> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateDonationMethod(), responseObserver);
    }

    /**
     */
    default void updateDonation(ong.inventory.Inventory.UpdateDonationRequest request,
        io.grpc.stub.StreamObserver<ong.inventory.Inventory.DonationItem> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateDonationMethod(), responseObserver);
    }

    /**
     * <pre>
     * baja lógica
     * </pre>
     */
    default void deleteDonation(ong.inventory.Inventory.DonationIdRequest request,
        io.grpc.stub.StreamObserver<ong.inventory.Inventory.DonationItem> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteDonationMethod(), responseObserver);
    }

    /**
     */
    default void listDonations(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ong.inventory.Inventory.DonationList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListDonationsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DonacionesService.
   */
  public static abstract class DonacionesServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DonacionesServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DonacionesService.
   */
  public static final class DonacionesServiceStub
      extends io.grpc.stub.AbstractAsyncStub<DonacionesServiceStub> {
    private DonacionesServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DonacionesServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DonacionesServiceStub(channel, callOptions);
    }

    /**
     */
    public void createDonation(ong.inventory.Inventory.CreateDonationRequest request,
        io.grpc.stub.StreamObserver<ong.inventory.Inventory.DonationItem> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateDonationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateDonation(ong.inventory.Inventory.UpdateDonationRequest request,
        io.grpc.stub.StreamObserver<ong.inventory.Inventory.DonationItem> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateDonationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * baja lógica
     * </pre>
     */
    public void deleteDonation(ong.inventory.Inventory.DonationIdRequest request,
        io.grpc.stub.StreamObserver<ong.inventory.Inventory.DonationItem> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteDonationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listDonations(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ong.inventory.Inventory.DonationList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListDonationsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DonacionesService.
   */
  public static final class DonacionesServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<DonacionesServiceBlockingV2Stub> {
    private DonacionesServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DonacionesServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DonacionesServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public ong.inventory.Inventory.DonationItem createDonation(ong.inventory.Inventory.CreateDonationRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCreateDonationMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.inventory.Inventory.DonationItem updateDonation(ong.inventory.Inventory.UpdateDonationRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getUpdateDonationMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * baja lógica
     * </pre>
     */
    public ong.inventory.Inventory.DonationItem deleteDonation(ong.inventory.Inventory.DonationIdRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getDeleteDonationMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.inventory.Inventory.DonationList listDonations(com.google.protobuf.Empty request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getListDonationsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service DonacionesService.
   */
  public static final class DonacionesServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DonacionesServiceBlockingStub> {
    private DonacionesServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DonacionesServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DonacionesServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public ong.inventory.Inventory.DonationItem createDonation(ong.inventory.Inventory.CreateDonationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateDonationMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.inventory.Inventory.DonationItem updateDonation(ong.inventory.Inventory.UpdateDonationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateDonationMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * baja lógica
     * </pre>
     */
    public ong.inventory.Inventory.DonationItem deleteDonation(ong.inventory.Inventory.DonationIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteDonationMethod(), getCallOptions(), request);
    }

    /**
     */
    public ong.inventory.Inventory.DonationList listDonations(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListDonationsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DonacionesService.
   */
  public static final class DonacionesServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<DonacionesServiceFutureStub> {
    private DonacionesServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DonacionesServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DonacionesServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.inventory.Inventory.DonationItem> createDonation(
        ong.inventory.Inventory.CreateDonationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateDonationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.inventory.Inventory.DonationItem> updateDonation(
        ong.inventory.Inventory.UpdateDonationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateDonationMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * baja lógica
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.inventory.Inventory.DonationItem> deleteDonation(
        ong.inventory.Inventory.DonationIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteDonationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ong.inventory.Inventory.DonationList> listDonations(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListDonationsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_DONATION = 0;
  private static final int METHODID_UPDATE_DONATION = 1;
  private static final int METHODID_DELETE_DONATION = 2;
  private static final int METHODID_LIST_DONATIONS = 3;

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
        case METHODID_CREATE_DONATION:
          serviceImpl.createDonation((ong.inventory.Inventory.CreateDonationRequest) request,
              (io.grpc.stub.StreamObserver<ong.inventory.Inventory.DonationItem>) responseObserver);
          break;
        case METHODID_UPDATE_DONATION:
          serviceImpl.updateDonation((ong.inventory.Inventory.UpdateDonationRequest) request,
              (io.grpc.stub.StreamObserver<ong.inventory.Inventory.DonationItem>) responseObserver);
          break;
        case METHODID_DELETE_DONATION:
          serviceImpl.deleteDonation((ong.inventory.Inventory.DonationIdRequest) request,
              (io.grpc.stub.StreamObserver<ong.inventory.Inventory.DonationItem>) responseObserver);
          break;
        case METHODID_LIST_DONATIONS:
          serviceImpl.listDonations((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<ong.inventory.Inventory.DonationList>) responseObserver);
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
          getCreateDonationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ong.inventory.Inventory.CreateDonationRequest,
              ong.inventory.Inventory.DonationItem>(
                service, METHODID_CREATE_DONATION)))
        .addMethod(
          getUpdateDonationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ong.inventory.Inventory.UpdateDonationRequest,
              ong.inventory.Inventory.DonationItem>(
                service, METHODID_UPDATE_DONATION)))
        .addMethod(
          getDeleteDonationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ong.inventory.Inventory.DonationIdRequest,
              ong.inventory.Inventory.DonationItem>(
                service, METHODID_DELETE_DONATION)))
        .addMethod(
          getListDonationsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              ong.inventory.Inventory.DonationList>(
                service, METHODID_LIST_DONATIONS)))
        .build();
  }

  private static abstract class DonacionesServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DonacionesServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ong.inventory.Inventory.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DonacionesService");
    }
  }

  private static final class DonacionesServiceFileDescriptorSupplier
      extends DonacionesServiceBaseDescriptorSupplier {
    DonacionesServiceFileDescriptorSupplier() {}
  }

  private static final class DonacionesServiceMethodDescriptorSupplier
      extends DonacionesServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DonacionesServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (DonacionesServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DonacionesServiceFileDescriptorSupplier())
              .addMethod(getCreateDonationMethod())
              .addMethod(getUpdateDonationMethod())
              .addMethod(getDeleteDonationMethod())
              .addMethod(getListDonationsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
