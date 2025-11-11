"""Client and server classes corresponding to protobuf-defined services."""
import grpc
import warnings

from . import aspirantes_pb2 as aspirantes__pb2
from google.protobuf import empty_pb2 as google_dot_protobuf_dot_empty__pb2

GRPC_GENERATED_VERSION = '1.76.0'
GRPC_VERSION = grpc.__version__
_version_not_supported = False

# Commented out version check to avoid compatibility issues
# try:
#     from grpc._utilities import first_version_is_lower
#     _version_not_supported = first_version_is_lower(GRPC_VERSION, GRPC_GENERATED_VERSION)
# except ImportError:
#     _version_not_supported = True
# 
# if _version_not_supported:
#     raise RuntimeError(
#         f'The grpc package installed is at version {GRPC_VERSION},'
#         + ' but the generated code in aspirantes_pb2_grpc.py depends on'
#         + f' grpcio>={GRPC_GENERATED_VERSION}.'
#         + f' Please upgrade your grpc module to grpcio>={GRPC_GENERATED_VERSION}'
#         + f' or downgrade your generated code using grpcio-tools<={GRPC_VERSION}.'
#     )


class AspirantesServiceStub(object):
    """Missing associated documentation comment in .proto file."""

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.CreateAspirante = channel.unary_unary(
                '/ong.aspirantes.AspirantesService/CreateAspirante',
                request_serializer=aspirantes__pb2.CreateAspiranteRequest.SerializeToString,
                response_deserializer=aspirantes__pb2.Aspirante.FromString,
                _registered_method=True)
        self.GetAspirante = channel.unary_unary(
                '/ong.aspirantes.AspirantesService/GetAspirante',
                request_serializer=aspirantes__pb2.AspiranteIdRequest.SerializeToString,
                response_deserializer=aspirantes__pb2.Aspirante.FromString,
                _registered_method=True)
        self.ListAspirantesPendientes = channel.unary_unary(
                '/ong.aspirantes.AspirantesService/ListAspirantesPendientes',
                request_serializer=google_dot_protobuf_dot_empty__pb2.Empty.SerializeToString,
                response_deserializer=aspirantes__pb2.AspiranteList.FromString,
                _registered_method=True)
        self.RejectAspirante = channel.unary_unary(
                '/ong.aspirantes.AspirantesService/RejectAspirante',
                request_serializer=aspirantes__pb2.RejectAspiranteRequest.SerializeToString,
                response_deserializer=aspirantes__pb2.Aspirante.FromString,
                _registered_method=True)
        self.AcceptAspirante = channel.unary_unary(
                '/ong.aspirantes.AspirantesService/AcceptAspirante',
                request_serializer=aspirantes__pb2.AcceptAspiranteRequest.SerializeToString,
                response_deserializer=aspirantes__pb2.Aspirante.FromString,
                _registered_method=True)


class AspirantesServiceServicer(object):
    """Missing associated documentation comment in .proto file."""

    def CreateAspirante(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def GetAspirante(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def ListAspirantesPendientes(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def RejectAspirante(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def AcceptAspirante(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_AspirantesServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
            'CreateAspirante': grpc.unary_unary_rpc_method_handler(
                    servicer.CreateAspirante,
                    request_deserializer=aspirantes__pb2.CreateAspiranteRequest.FromString,
                    response_serializer=aspirantes__pb2.Aspirante.SerializeToString,
            ),
            'GetAspirante': grpc.unary_unary_rpc_method_handler(
                    servicer.GetAspirante,
                    request_deserializer=aspirantes__pb2.AspiranteIdRequest.FromString,
                    response_serializer=aspirantes__pb2.Aspirante.SerializeToString,
            ),
            'ListAspirantesPendientes': grpc.unary_unary_rpc_method_handler(
                    servicer.ListAspirantesPendientes,
                    request_deserializer=google_dot_protobuf_dot_empty__pb2.Empty.FromString,
                    response_serializer=aspirantes__pb2.AspiranteList.SerializeToString,
            ),
            'RejectAspirante': grpc.unary_unary_rpc_method_handler(
                    servicer.RejectAspirante,
                    request_deserializer=aspirantes__pb2.RejectAspiranteRequest.FromString,
                    response_serializer=aspirantes__pb2.Aspirante.SerializeToString,
            ),
            'AcceptAspirante': grpc.unary_unary_rpc_method_handler(
                    servicer.AcceptAspirante,
                    request_deserializer=aspirantes__pb2.AcceptAspiranteRequest.FromString,
                    response_serializer=aspirantes__pb2.Aspirante.SerializeToString,
            ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
            'ong.aspirantes.AspirantesService', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))
    server.add_registered_method_handlers('ong.aspirantes.AspirantesService', rpc_method_handlers)


 # This class is part of an EXPERIMENTAL API.
class AspirantesService(object):
    """Missing associated documentation comment in .proto file."""

    @staticmethod
    def CreateAspirante(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/ong.aspirantes.AspirantesService/CreateAspirante',
            aspirantes__pb2.CreateAspiranteRequest.SerializeToString,
            aspirantes__pb2.Aspirante.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def GetAspirante(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/ong.aspirantes.AspirantesService/GetAspirante',
            aspirantes__pb2.AspiranteIdRequest.SerializeToString,
            aspirantes__pb2.Aspirante.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def ListAspirantesPendientes(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/ong.aspirantes.AspirantesService/ListAspirantesPendientes',
            google_dot_protobuf_dot_empty__pb2.Empty.SerializeToString,
            aspirantes__pb2.AspiranteList.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def RejectAspirante(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/ong.aspirantes.AspirantesService/RejectAspirante',
            aspirantes__pb2.RejectAspiranteRequest.SerializeToString,
            aspirantes__pb2.Aspirante.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def AcceptAspirante(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/ong.aspirantes.AspirantesService/AcceptAspirante',
            aspirantes__pb2.AcceptAspiranteRequest.SerializeToString,
            aspirantes__pb2.Aspirante.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)