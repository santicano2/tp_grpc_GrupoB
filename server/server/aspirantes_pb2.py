# -*- coding: utf-8 -*-
# source: aspirantes.proto
# Protobuf Python Version: 6.31.1
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
# from google.protobuf import runtime_version as _runtime_version  # Commented for Docker compatibility
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
# _runtime_version.ValidateProtobufRuntimeVersion(  # Commented for Docker compatibility
#     _runtime_version.Domain.PUBLIC,
#     6,
#     31,
#     1,
#     '',
#     'aspirantes.proto'
# )
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from google.protobuf import empty_pb2 as google_dot_protobuf_dot_empty__pb2


DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x10\x61spirantes.proto\x12\x0eong.aspirantes\x1a\x1bgoogle/protobuf/empty.proto\"\x8e\x02\n\tAspirante\x12\n\n\x02id\x18\x01 \x01(\x05\x12\x0e\n\x06nombre\x18\x02 \x01(\t\x12\x10\n\x08\x61pellido\x18\x03 \x01(\t\x12\x0c\n\x04\x65\x64\x61\x64\x18\x04 \x01(\x05\x12\x10\n\x08telefono\x18\x05 \x01(\t\x12\r\n\x05\x65mail\x18\x06 \x01(\t\x12\x0e\n\x06motivo\x18\x07 \x01(\t\x12/\n\x06\x65stado\x18\x08 \x01(\x0e\x32\x1f.ong.aspirantes.EstadoAspirante\x12\x16\n\x0emotivo_rechazo\x18\t \x01(\t\x12\x17\n\x0f\x66\x65\x63ha_solicitud\x18\n \x01(\t\x12\x1b\n\x13\x66\x65\x63ha_procesamiento\x18\x0b \x01(\t\x12\x15\n\rprocesado_por\x18\x0c \x01(\t\"y\n\x16\x43reateAspiranteRequest\x12\x0e\n\x06nombre\x18\x01 \x01(\t\x12\x10\n\x08\x61pellido\x18\x02 \x01(\t\x12\x0c\n\x04\x65\x64\x61\x64\x18\x03 \x01(\x05\x12\x10\n\x08telefono\x18\x04 \x01(\t\x12\r\n\x05\x65mail\x18\x05 \x01(\t\x12\x0e\n\x06motivo\x18\x06 \x01(\t\"8\n\x12\x41spiranteIdRequest\x12\x16\n\x0e\x61\x63tor_username\x18\x01 \x01(\t\x12\n\n\x02id\x18\x02 \x01(\x05\"T\n\x16RejectAspiranteRequest\x12\x16\n\x0e\x61\x63tor_username\x18\x01 \x01(\t\x12\n\n\x02id\x18\x02 \x01(\x05\x12\x16\n\x0emotivo_rechazo\x18\x03 \x01(\t\"<\n\x16\x41\x63\x63\x65ptAspiranteRequest\x12\x16\n\x0e\x61\x63tor_username\x18\x01 \x01(\t\x12\n\n\x02id\x18\x02 \x01(\x05\">\n\rAspiranteList\x12-\n\naspirantes\x18\x01 \x03(\x0b\x32\x19.ong.aspirantes.Aspirante*=\n\x0f\x45stadoAspirante\x12\r\n\tPENDIENTE\x10\x00\x12\x0c\n\x08\x41\x43\x45PTADO\x10\x01\x12\r\n\tRECHAZADO\x10\x02\x32\xb7\x03\n\x11\x41spirantesService\x12T\n\x0f\x43reateAspirante\x12&.ong.aspirantes.CreateAspiranteRequest\x1a\x19.ong.aspirantes.Aspirante\x12M\n\x0cGetAspirante\x12\".ong.aspirantes.AspiranteIdRequest\x1a\x19.ong.aspirantes.Aspirante\x12Q\n\x18ListAspirantesPendientes\x12\x16.google.protobuf.Empty\x1a\x1d.ong.aspirantes.AspiranteList\x12T\n\x0fRejectAspirante\x12&.ong.aspirantes.RejectAspiranteRequest\x1a\x19.ong.aspirantes.Aspirante\x12T\n\x0f\x41\x63\x63\x65ptAspirante\x12&.ong.aspirantes.AcceptAspiranteRequest\x1a\x19.ong.aspirantes.Aspiranteb\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'aspirantes_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  DESCRIPTOR._loaded_options = None
  _globals['_ESTADOASPIRANTE']._serialized_start=731
  _globals['_ESTADOASPIRANTE']._serialized_end=792
  _globals['_ASPIRANTE']._serialized_start=66
  _globals['_ASPIRANTE']._serialized_end=336
  _globals['_CREATEASPIRANTEREQUEST']._serialized_start=338
  _globals['_CREATEASPIRANTEREQUEST']._serialized_end=459
  _globals['_ASPIRANTEIDREQUEST']._serialized_start=461
  _globals['_ASPIRANTEIDREQUEST']._serialized_end=517
  _globals['_REJECTASPIRANTEREQUEST']._serialized_start=519
  _globals['_REJECTASPIRANTEREQUEST']._serialized_end=603
  _globals['_ACCEPTASPIRANTEREQUEST']._serialized_start=605
  _globals['_ACCEPTASPIRANTEREQUEST']._serialized_end=665
  _globals['_ASPIRANTELIST']._serialized_start=667
  _globals['_ASPIRANTELIST']._serialized_end=729
  _globals['_ASPIRANTESSERVICE']._serialized_start=795
  _globals['_ASPIRANTESSERVICE']._serialized_end=1234
# @@protoc_insertion_point(module_scope)
