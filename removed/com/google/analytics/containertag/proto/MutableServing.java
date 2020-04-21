package com.google.analytics.containertag.proto;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.analytics.midtier.proto.containertag.MutableTypeSystem;
import com.google.tagmanager.protobuf.AbstractMutableMessageLite;
import com.google.tagmanager.protobuf.ByteString;
import com.google.tagmanager.protobuf.CodedInputStream;
import com.google.tagmanager.protobuf.CodedOutputStream;
import com.google.tagmanager.protobuf.ExtensionRegistryLite;
import com.google.tagmanager.protobuf.GeneratedMessageLite;
import com.google.tagmanager.protobuf.GeneratedMutableMessageLite;
import com.google.tagmanager.protobuf.Internal;
import com.google.tagmanager.protobuf.LazyStringArrayList;
import com.google.tagmanager.protobuf.LazyStringList;
import com.google.tagmanager.protobuf.MessageLite;
import com.google.tagmanager.protobuf.MutableMessageLite;
import com.google.tagmanager.protobuf.Parser;
import com.google.tagmanager.protobuf.WireFormat;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import org.apache.james.mime4j.field.datetime.parser.DateTimeParserConstants;

public final class MutableServing {
	private MutableServing() {
	}

	public static void registerAllExtensions(ExtensionRegistryLite registry) {
		registry.add(ServingValue.ext);
	}

	public enum ResourceType implements Internal.EnumLite {
		JS_RESOURCE(0, 1),
		NS_RESOURCE(1, 2),
		PIXEL_COLLECTION(2, 3),
		SET_COOKIE(3, 4),
		GET_COOKIE(4, 5),
		CLEAR_CACHE(5, 6),
		RAW_PROTO(6, 7);

		public static final int CLEAR_CACHE_VALUE = 6;
		public static final int GET_COOKIE_VALUE = 5;
		public static final int JS_RESOURCE_VALUE = 1;
		public static final int NS_RESOURCE_VALUE = 2;
		public static final int PIXEL_COLLECTION_VALUE = 3;
		public static final int RAW_PROTO_VALUE = 7;
		public static final int SET_COOKIE_VALUE = 4;
		private static Internal.EnumLiteMap<ResourceType> internalValueMap;
		private final int value;

		static {
			internalValueMap = new Internal.EnumLiteMap<ResourceType>() {
				public ResourceType findValueByNumber(int number) {
					return ResourceType.valueOf(number);
				}
			};
		}

		public final int getNumber() {
			return this.value;
		}

		public static ResourceType valueOf(int value2) {
			switch (value2) {
				case 1:
					return JS_RESOURCE;
				case 2:
					return NS_RESOURCE;
				case 3:
					return PIXEL_COLLECTION;
				case 4:
					return SET_COOKIE;
				case 5:
					return GET_COOKIE;
				case 6:
					return CLEAR_CACHE;
				case 7:
					return RAW_PROTO;
				default:
					return null;
			}
		}

		public static Internal.EnumLiteMap<ResourceType> internalGetValueMap() {
			return internalValueMap;
		}

		private ResourceType(int index, int value2) {
			this.value = value2;
		}
	}

	public enum ResourceState implements Internal.EnumLite {
		PREVIEW(0, 1),
		LIVE(1, 2);

		public static final int LIVE_VALUE = 2;
		public static final int PREVIEW_VALUE = 1;
		private static Internal.EnumLiteMap<ResourceState> internalValueMap;
		private final int value;

		static {
			internalValueMap = new Internal.EnumLiteMap<ResourceState>() {
				public ResourceState findValueByNumber(int number) {
					return ResourceState.valueOf(number);
				}
			};
		}

		public final int getNumber() {
			return this.value;
		}

		public static ResourceState valueOf(int value2) {
			switch (value2) {
				case 1:
					return PREVIEW;
				case 2:
					return LIVE;
				default:
					return null;
			}
		}

		public static Internal.EnumLiteMap<ResourceState> internalGetValueMap() {
			return internalValueMap;
		}

		private ResourceState(int index, int value2) {
			this.value = value2;
		}
	}

	public static final class Container extends GeneratedMutableMessageLite<Container> implements MutableMessageLite {
		public static final int CONTAINER_ID_FIELD_NUMBER = 3;
		public static final int JS_RESOURCE_FIELD_NUMBER = 1;
		public static final int STATE_FIELD_NUMBER = 4;
		public static final int VERSION_FIELD_NUMBER = 5;
		private static final Container defaultInstance = new Container(true);
		public static Parser<Container> PARSER = AbstractMutableMessageLite.internalNewParserForType(defaultInstance);
		private static volatile MessageLite immutableDefault = null;
		private static final long serialVersionUID = 0;
		private int bitField0_;
		private Object containerId_ = Internal.EMPTY_BYTE_ARRAY;
		private Resource jsResource_;
		private ResourceState state_ = ResourceState.PREVIEW;
		private Object version_ = Internal.EMPTY_BYTE_ARRAY;

		private Container() {
			initFields();
		}

		private Container(boolean noInit) {
		}

		public Container newMessageForType() {
			return new Container();
		}

		public static Container newMessage() {
			return new Container();
		}

		private void initFields() {
			this.jsResource_ = Resource.getDefaultInstance();
			this.state_ = ResourceState.PREVIEW;
		}

		public static Container getDefaultInstance() {
			return defaultInstance;
		}

		public final Container getDefaultInstanceForType() {
			return defaultInstance;
		}

		public Parser<Container> getParserForType() {
			return PARSER;
		}

		private void ensureJsResourceInitialized() {
			if (this.jsResource_ == Resource.getDefaultInstance()) {
				this.jsResource_ = Resource.newMessage();
			}
		}

		public boolean hasJsResource() {
			return (this.bitField0_ & 1) == 1;
		}

		public Resource getJsResource() {
			return this.jsResource_;
		}

		public Resource getMutableJsResource() {
			assertMutable();
			ensureJsResourceInitialized();
			this.bitField0_ |= 1;
			return this.jsResource_;
		}

		public Container setJsResource(Resource value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 1;
			this.jsResource_ = value;
			return this;
		}

		public Container clearJsResource() {
			assertMutable();
			this.bitField0_ &= -2;
			if (this.jsResource_ != Resource.getDefaultInstance()) {
				this.jsResource_.clear();
			}
			return this;
		}

		public boolean hasContainerId() {
			return (this.bitField0_ & 2) == 2;
		}

		public String getContainerId() {
			Object ref = this.containerId_;
			if (ref instanceof String) {
				return (String) ref;
			}
			byte[] byteArray = (byte[]) ref;
			String s = Internal.toStringUtf8(byteArray);
			if (Internal.isValidUtf8(byteArray)) {
				this.containerId_ = s;
			}
			return s;
		}

		public byte[] getContainerIdAsBytes() {
			Object ref = this.containerId_;
			if (!(ref instanceof String)) {
				return (byte[]) ref;
			}
			byte[] byteArray = Internal.toByteArray((String) ref);
			this.containerId_ = byteArray;
			return byteArray;
		}

		public Container setContainerId(String value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 2;
			this.containerId_ = value;
			return this;
		}

		public Container setContainerIdAsBytes(byte[] value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 2;
			this.containerId_ = value;
			return this;
		}

		public Container clearContainerId() {
			assertMutable();
			this.bitField0_ &= -3;
			this.containerId_ = Internal.EMPTY_BYTE_ARRAY;
			return this;
		}

		public boolean hasState() {
			return (this.bitField0_ & 4) == 4;
		}

		public ResourceState getState() {
			return this.state_;
		}

		public Container setState(ResourceState value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 4;
			this.state_ = value;
			return this;
		}

		public Container clearState() {
			assertMutable();
			this.bitField0_ &= -5;
			this.state_ = ResourceState.PREVIEW;
			return this;
		}

		public boolean hasVersion() {
			return (this.bitField0_ & 8) == 8;
		}

		public String getVersion() {
			Object ref = this.version_;
			if (ref instanceof String) {
				return (String) ref;
			}
			byte[] byteArray = (byte[]) ref;
			String s = Internal.toStringUtf8(byteArray);
			if (Internal.isValidUtf8(byteArray)) {
				this.version_ = s;
			}
			return s;
		}

		public byte[] getVersionAsBytes() {
			Object ref = this.version_;
			if (!(ref instanceof String)) {
				return (byte[]) ref;
			}
			byte[] byteArray = Internal.toByteArray((String) ref);
			this.version_ = byteArray;
			return byteArray;
		}

		public Container setVersion(String value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 8;
			this.version_ = value;
			return this;
		}

		public Container setVersionAsBytes(byte[] value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 8;
			this.version_ = value;
			return this;
		}

		public Container clearVersion() {
			assertMutable();
			this.bitField0_ &= -9;
			this.version_ = Internal.EMPTY_BYTE_ARRAY;
			return this;
		}

		public final boolean isInitialized() {
			if (hasJsResource() && hasContainerId() && hasState() && getJsResource().isInitialized()) {
				return true;
			}
			return false;
		}

		public Container clone() {
			return newMessageForType().mergeFrom(this);
		}

		public Container mergeFrom(Container other) {
			if (this == other) {
				throw new IllegalArgumentException("mergeFrom(message) called on the same message.");
			}
			assertMutable();
			if (other != getDefaultInstance()) {
				if (other.hasJsResource()) {
					ensureJsResourceInitialized();
					this.jsResource_.mergeFrom(other.getJsResource());
					this.bitField0_ |= 1;
				}
				if (other.hasContainerId()) {
					this.bitField0_ |= 2;
					if (other.containerId_ instanceof String) {
						this.containerId_ = other.containerId_;
					} else {
						byte[] ba = (byte[]) other.containerId_;
						this.containerId_ = Arrays.copyOf(ba, ba.length);
					}
				}
				if (other.hasState()) {
					setState(other.getState());
				}
				if (other.hasVersion()) {
					this.bitField0_ |= 8;
					if (other.version_ instanceof String) {
						this.version_ = other.version_;
					} else {
						byte[] ba2 = (byte[]) other.version_;
						this.version_ = Arrays.copyOf(ba2, ba2.length);
					}
				}
				this.unknownFields = this.unknownFields.concat(other.unknownFields);
			}
			return this;
		}

		public boolean mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
			assertMutable();
			try {
				ByteString.Output unknownFieldsOutput = ByteString.newOutput();
				CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance((OutputStream) unknownFieldsOutput);
				boolean done = false;
				while (!done) {
					int tag = input.readTag();
					switch (tag) {
						case 0:
							done = true;
							break;
						case 10:
							if (this.jsResource_ == Resource.getDefaultInstance()) {
								this.jsResource_ = Resource.newMessage();
							}
							this.bitField0_ |= 1;
							input.readMessage((MutableMessageLite) this.jsResource_, extensionRegistry);
							break;
						case 26:
							this.bitField0_ |= 2;
							this.containerId_ = input.readByteArray();
							break;
						case 32:
							int rawValue = input.readEnum();
							ResourceState value = ResourceState.valueOf(rawValue);
							if (value != null) {
								this.bitField0_ |= 4;
								this.state_ = value;
								break;
							} else {
								unknownFieldsCodedOutput.writeRawVarint32(tag);
								unknownFieldsCodedOutput.writeRawVarint32(rawValue);
								break;
							}
						case 42:
							this.bitField0_ |= 8;
							this.version_ = input.readByteArray();
							break;
						default:
							if (parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
								break;
							} else {
								done = true;
								break;
							}
					}
				}
				unknownFieldsCodedOutput.flush();
				this.unknownFields = unknownFieldsOutput.toByteString();
				return true;
			} catch (IOException e) {
				return false;
			}
		}

		public void writeToWithCachedSizes(CodedOutputStream output) throws IOException {
			int bytesWrittenBefore = output.getTotalBytesWritten();
			output.writeMessageWithCachedSizes(1, this.jsResource_);
			output.writeByteArray(3, getContainerIdAsBytes());
			output.writeEnum(4, this.state_.getNumber());
			if ((this.bitField0_ & 8) == 8) {
				output.writeByteArray(5, getVersionAsBytes());
			}
			output.writeRawBytes(this.unknownFields);
			if (getCachedSize() != output.getTotalBytesWritten() - bytesWrittenBefore) {
				throw new RuntimeException("Serialized size doesn't match cached size. You may forget to call getSerializedSize() or the message is being modified concurrently.");
			}
		}

		public int getSerializedSize() {
			int size = 0 + CodedOutputStream.computeMessageSize(1, this.jsResource_) + CodedOutputStream.computeByteArraySize(3, getContainerIdAsBytes()) + CodedOutputStream.computeEnumSize(4, this.state_.getNumber());
			if ((this.bitField0_ & 8) == 8) {
				size += CodedOutputStream.computeByteArraySize(5, getVersionAsBytes());
			}
			int size2 = size + this.unknownFields.size();
			this.cachedSize = size2;
			return size2;
		}

		/* access modifiers changed from: protected */
		public Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public Container clear() {
			assertMutable();
			super.clear();
			if (this.jsResource_ != Resource.getDefaultInstance()) {
				this.jsResource_.clear();
			}
			this.bitField0_ &= -2;
			this.containerId_ = Internal.EMPTY_BYTE_ARRAY;
			this.bitField0_ &= -3;
			this.state_ = ResourceState.PREVIEW;
			this.bitField0_ &= -5;
			this.version_ = Internal.EMPTY_BYTE_ARRAY;
			this.bitField0_ &= -9;
			return this;
		}

		public boolean equals(Object obj) {
			boolean result;
			boolean result2;
			boolean result3;
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof Container)) {
				return super.equals(obj);
			}
			Container other = (Container) obj;
			boolean result4 = 1 != 0 && hasJsResource() == other.hasJsResource();
			if (hasJsResource()) {
				result4 = result4 && getJsResource().equals(other.getJsResource());
			}
			if (!result4 || hasContainerId() != other.hasContainerId()) {
				result = false;
			} else {
				result = true;
			}
			if (hasContainerId()) {
				if (!result || !getContainerId().equals(other.getContainerId())) {
					result = false;
				} else {
					result = true;
				}
			}
			if (!result || hasState() != other.hasState()) {
				result2 = false;
			} else {
				result2 = true;
			}
			if (hasState()) {
				if (!result2 || getState() != other.getState()) {
					result2 = false;
				} else {
					result2 = true;
				}
			}
			if (!result2 || hasVersion() != other.hasVersion()) {
				result3 = false;
			} else {
				result3 = true;
			}
			if (hasVersion()) {
				if (!result3 || !getVersion().equals(other.getVersion())) {
					result3 = false;
				} else {
					result3 = true;
				}
			}
			return result3;
		}

		public int hashCode() {
			int hash = 41;
			if (hasJsResource()) {
				int hash2 = 1517 + 1;
				hash = 80454 + getJsResource().hashCode();
			}
			if (hasContainerId()) {
				hash = (((hash * 37) + 3) * 53) + getContainerId().hashCode();
			}
			if (hasState()) {
				hash = (((hash * 37) + 4) * 53) + Internal.hashEnum(getState());
			}
			if (hasVersion()) {
				hash = (((hash * 37) + 5) * 53) + getVersion().hashCode();
			}
			return (hash * 29) + this.unknownFields.hashCode();
		}

		static {
			defaultInstance.initFields();
			defaultInstance.makeImmutable();
		}

		/* access modifiers changed from: protected */
		public MessageLite internalImmutableDefault() {
			if (immutableDefault == null) {
				immutableDefault = internalImmutableDefault("com.google.analytics.containertag.proto.Serving$Container");
			}
			return immutableDefault;
		}
	}

	public static final class ServingValue extends GeneratedMutableMessageLite<ServingValue> implements MutableMessageLite {
		public static final int EXT_FIELD_NUMBER = 101;
		public static final int LIST_ITEM_FIELD_NUMBER = 1;
		public static final int MACRO_NAME_REFERENCE_FIELD_NUMBER = 6;
		public static final int MACRO_REFERENCE_FIELD_NUMBER = 4;
		public static final int MAP_KEY_FIELD_NUMBER = 2;
		public static final int MAP_VALUE_FIELD_NUMBER = 3;
		public static final int TEMPLATE_TOKEN_FIELD_NUMBER = 5;
		private static final ServingValue defaultInstance = new ServingValue(true);
		public static Parser<ServingValue> PARSER = AbstractMutableMessageLite.internalNewParserForType(defaultInstance);
		public static final GeneratedMessageLite.GeneratedExtension<MutableTypeSystem.Value, ServingValue> ext = GeneratedMessageLite.newSingularGeneratedExtension(MutableTypeSystem.Value.getDefaultInstance(), getDefaultInstance(), getDefaultInstance(), (Internal.EnumLiteMap<?>) null, 101, WireFormat.FieldType.MESSAGE, ServingValue.class);
		private static volatile MessageLite immutableDefault = null;
		private static final long serialVersionUID = 0;
		private int bitField0_;
		private List<Integer> listItem_ = null;
		private int macroNameReference_;
		private int macroReference_;
		private List<Integer> mapKey_ = null;
		private List<Integer> mapValue_ = null;
		private List<Integer> templateToken_ = null;

		private ServingValue() {
			initFields();
		}

		private ServingValue(boolean noInit) {
		}

		public ServingValue newMessageForType() {
			return new ServingValue();
		}

		public static ServingValue newMessage() {
			return new ServingValue();
		}

		private void initFields() {
		}

		public static ServingValue getDefaultInstance() {
			return defaultInstance;
		}

		public final ServingValue getDefaultInstanceForType() {
			return defaultInstance;
		}

		public Parser<ServingValue> getParserForType() {
			return PARSER;
		}

		private void ensureListItemInitialized() {
			if (this.listItem_ == null) {
				this.listItem_ = new ArrayList();
			}
		}

		public List<Integer> getListItemList() {
			if (this.listItem_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.listItem_);
		}

		public List<Integer> getMutableListItemList() {
			assertMutable();
			ensureListItemInitialized();
			return this.listItem_;
		}

		public int getListItemCount() {
			if (this.listItem_ == null) {
				return 0;
			}
			return this.listItem_.size();
		}

		public int getListItem(int index) {
			return this.listItem_.get(index).intValue();
		}

		public ServingValue setListItem(int index, int value) {
			assertMutable();
			ensureListItemInitialized();
			this.listItem_.set(index, Integer.valueOf(value));
			return this;
		}

		public ServingValue addListItem(int value) {
			assertMutable();
			ensureListItemInitialized();
			this.listItem_.add(Integer.valueOf(value));
			return this;
		}

		public ServingValue addAllListItem(Iterable<? extends Integer> values) {
			assertMutable();
			ensureListItemInitialized();
			AbstractMutableMessageLite.addAll(values, this.listItem_);
			return this;
		}

		public ServingValue clearListItem() {
			assertMutable();
			this.listItem_ = null;
			return this;
		}

		private void ensureMapKeyInitialized() {
			if (this.mapKey_ == null) {
				this.mapKey_ = new ArrayList();
			}
		}

		public List<Integer> getMapKeyList() {
			if (this.mapKey_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.mapKey_);
		}

		public List<Integer> getMutableMapKeyList() {
			assertMutable();
			ensureMapKeyInitialized();
			return this.mapKey_;
		}

		public int getMapKeyCount() {
			if (this.mapKey_ == null) {
				return 0;
			}
			return this.mapKey_.size();
		}

		public int getMapKey(int index) {
			return this.mapKey_.get(index).intValue();
		}

		public ServingValue setMapKey(int index, int value) {
			assertMutable();
			ensureMapKeyInitialized();
			this.mapKey_.set(index, Integer.valueOf(value));
			return this;
		}

		public ServingValue addMapKey(int value) {
			assertMutable();
			ensureMapKeyInitialized();
			this.mapKey_.add(Integer.valueOf(value));
			return this;
		}

		public ServingValue addAllMapKey(Iterable<? extends Integer> values) {
			assertMutable();
			ensureMapKeyInitialized();
			AbstractMutableMessageLite.addAll(values, this.mapKey_);
			return this;
		}

		public ServingValue clearMapKey() {
			assertMutable();
			this.mapKey_ = null;
			return this;
		}

		private void ensureMapValueInitialized() {
			if (this.mapValue_ == null) {
				this.mapValue_ = new ArrayList();
			}
		}

		public List<Integer> getMapValueList() {
			if (this.mapValue_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.mapValue_);
		}

		public List<Integer> getMutableMapValueList() {
			assertMutable();
			ensureMapValueInitialized();
			return this.mapValue_;
		}

		public int getMapValueCount() {
			if (this.mapValue_ == null) {
				return 0;
			}
			return this.mapValue_.size();
		}

		public int getMapValue(int index) {
			return this.mapValue_.get(index).intValue();
		}

		public ServingValue setMapValue(int index, int value) {
			assertMutable();
			ensureMapValueInitialized();
			this.mapValue_.set(index, Integer.valueOf(value));
			return this;
		}

		public ServingValue addMapValue(int value) {
			assertMutable();
			ensureMapValueInitialized();
			this.mapValue_.add(Integer.valueOf(value));
			return this;
		}

		public ServingValue addAllMapValue(Iterable<? extends Integer> values) {
			assertMutable();
			ensureMapValueInitialized();
			AbstractMutableMessageLite.addAll(values, this.mapValue_);
			return this;
		}

		public ServingValue clearMapValue() {
			assertMutable();
			this.mapValue_ = null;
			return this;
		}

		public boolean hasMacroReference() {
			return (this.bitField0_ & 1) == 1;
		}

		public int getMacroReference() {
			return this.macroReference_;
		}

		public ServingValue setMacroReference(int value) {
			assertMutable();
			this.bitField0_ |= 1;
			this.macroReference_ = value;
			return this;
		}

		public ServingValue clearMacroReference() {
			assertMutable();
			this.bitField0_ &= -2;
			this.macroReference_ = 0;
			return this;
		}

		private void ensureTemplateTokenInitialized() {
			if (this.templateToken_ == null) {
				this.templateToken_ = new ArrayList();
			}
		}

		public List<Integer> getTemplateTokenList() {
			if (this.templateToken_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.templateToken_);
		}

		public List<Integer> getMutableTemplateTokenList() {
			assertMutable();
			ensureTemplateTokenInitialized();
			return this.templateToken_;
		}

		public int getTemplateTokenCount() {
			if (this.templateToken_ == null) {
				return 0;
			}
			return this.templateToken_.size();
		}

		public int getTemplateToken(int index) {
			return this.templateToken_.get(index).intValue();
		}

		public ServingValue setTemplateToken(int index, int value) {
			assertMutable();
			ensureTemplateTokenInitialized();
			this.templateToken_.set(index, Integer.valueOf(value));
			return this;
		}

		public ServingValue addTemplateToken(int value) {
			assertMutable();
			ensureTemplateTokenInitialized();
			this.templateToken_.add(Integer.valueOf(value));
			return this;
		}

		public ServingValue addAllTemplateToken(Iterable<? extends Integer> values) {
			assertMutable();
			ensureTemplateTokenInitialized();
			AbstractMutableMessageLite.addAll(values, this.templateToken_);
			return this;
		}

		public ServingValue clearTemplateToken() {
			assertMutable();
			this.templateToken_ = null;
			return this;
		}

		public boolean hasMacroNameReference() {
			return (this.bitField0_ & 2) == 2;
		}

		public int getMacroNameReference() {
			return this.macroNameReference_;
		}

		public ServingValue setMacroNameReference(int value) {
			assertMutable();
			this.bitField0_ |= 2;
			this.macroNameReference_ = value;
			return this;
		}

		public ServingValue clearMacroNameReference() {
			assertMutable();
			this.bitField0_ &= -3;
			this.macroNameReference_ = 0;
			return this;
		}

		public final boolean isInitialized() {
			return true;
		}

		public ServingValue clone() {
			return newMessageForType().mergeFrom(this);
		}

		public ServingValue mergeFrom(ServingValue other) {
			if (this == other) {
				throw new IllegalArgumentException("mergeFrom(message) called on the same message.");
			}
			assertMutable();
			if (other != getDefaultInstance()) {
				if (other.listItem_ != null && !other.listItem_.isEmpty()) {
					ensureListItemInitialized();
					this.listItem_.addAll(other.listItem_);
				}
				if (other.mapKey_ != null && !other.mapKey_.isEmpty()) {
					ensureMapKeyInitialized();
					this.mapKey_.addAll(other.mapKey_);
				}
				if (other.mapValue_ != null && !other.mapValue_.isEmpty()) {
					ensureMapValueInitialized();
					this.mapValue_.addAll(other.mapValue_);
				}
				if (other.hasMacroReference()) {
					setMacroReference(other.getMacroReference());
				}
				if (other.templateToken_ != null && !other.templateToken_.isEmpty()) {
					ensureTemplateTokenInitialized();
					this.templateToken_.addAll(other.templateToken_);
				}
				if (other.hasMacroNameReference()) {
					setMacroNameReference(other.getMacroNameReference());
				}
				this.unknownFields = this.unknownFields.concat(other.unknownFields);
			}
			return this;
		}

		public boolean mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
			assertMutable();
			try {
				ByteString.Output unknownFieldsOutput = ByteString.newOutput();
				CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance((OutputStream) unknownFieldsOutput);
				boolean done = false;
				while (!done) {
					int tag = input.readTag();
					switch (tag) {
						case 0:
							done = true;
							break;
						case 8:
							if (this.listItem_ == null) {
								this.listItem_ = new ArrayList();
							}
							this.listItem_.add(Integer.valueOf(input.readInt32()));
							break;
						case 10:
							int limit = input.pushLimit(input.readRawVarint32());
							if (this.listItem_ == null) {
								this.listItem_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.listItem_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit);
							break;
						case 16:
							if (this.mapKey_ == null) {
								this.mapKey_ = new ArrayList();
							}
							this.mapKey_.add(Integer.valueOf(input.readInt32()));
							break;
						case 18:
							int limit2 = input.pushLimit(input.readRawVarint32());
							if (this.mapKey_ == null) {
								this.mapKey_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.mapKey_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit2);
							break;
						case 24:
							if (this.mapValue_ == null) {
								this.mapValue_ = new ArrayList();
							}
							this.mapValue_.add(Integer.valueOf(input.readInt32()));
							break;
						case 26:
							int limit3 = input.pushLimit(input.readRawVarint32());
							if (this.mapValue_ == null) {
								this.mapValue_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.mapValue_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit3);
							break;
						case 32:
							this.bitField0_ |= 1;
							this.macroReference_ = input.readInt32();
							break;
						case 40:
							if (this.templateToken_ == null) {
								this.templateToken_ = new ArrayList();
							}
							this.templateToken_.add(Integer.valueOf(input.readInt32()));
							break;
						case 42:
							int limit4 = input.pushLimit(input.readRawVarint32());
							if (this.templateToken_ == null) {
								this.templateToken_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.templateToken_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit4);
							break;
						case DateTimeParserConstants.ANY /*48*/:
							this.bitField0_ |= 2;
							this.macroNameReference_ = input.readInt32();
							break;
						default:
							if (parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
								break;
							} else {
								done = true;
								break;
							}
					}
				}
				unknownFieldsCodedOutput.flush();
				this.unknownFields = unknownFieldsOutput.toByteString();
				return true;
			} catch (IOException e) {
				return false;
			}
		}

		public void writeToWithCachedSizes(CodedOutputStream output) throws IOException {
			int bytesWrittenBefore = output.getTotalBytesWritten();
			if (this.listItem_ != null) {
				for (int i = 0; i < this.listItem_.size(); i++) {
					output.writeInt32(1, this.listItem_.get(i).intValue());
				}
			}
			if (this.mapKey_ != null) {
				for (int i2 = 0; i2 < this.mapKey_.size(); i2++) {
					output.writeInt32(2, this.mapKey_.get(i2).intValue());
				}
			}
			if (this.mapValue_ != null) {
				for (int i3 = 0; i3 < this.mapValue_.size(); i3++) {
					output.writeInt32(3, this.mapValue_.get(i3).intValue());
				}
			}
			if ((this.bitField0_ & 1) == 1) {
				output.writeInt32(4, this.macroReference_);
			}
			if (this.templateToken_ != null) {
				for (int i4 = 0; i4 < this.templateToken_.size(); i4++) {
					output.writeInt32(5, this.templateToken_.get(i4).intValue());
				}
			}
			if ((this.bitField0_ & 2) == 2) {
				output.writeInt32(6, this.macroNameReference_);
			}
			output.writeRawBytes(this.unknownFields);
			if (getCachedSize() != output.getTotalBytesWritten() - bytesWrittenBefore) {
				throw new RuntimeException("Serialized size doesn't match cached size. You may forget to call getSerializedSize() or the message is being modified concurrently.");
			}
		}

		public int getSerializedSize() {
			int size = 0;
			if (this.listItem_ != null && this.listItem_.size() > 0) {
				int dataSize = 0;
				for (int i = 0; i < this.listItem_.size(); i++) {
					dataSize += CodedOutputStream.computeInt32SizeNoTag(this.listItem_.get(i).intValue());
				}
				size = 0 + dataSize + (getListItemList().size() * 1);
			}
			if (this.mapKey_ != null && this.mapKey_.size() > 0) {
				int dataSize2 = 0;
				for (int i2 = 0; i2 < this.mapKey_.size(); i2++) {
					dataSize2 += CodedOutputStream.computeInt32SizeNoTag(this.mapKey_.get(i2).intValue());
				}
				size = size + dataSize2 + (getMapKeyList().size() * 1);
			}
			if (this.mapValue_ != null && this.mapValue_.size() > 0) {
				int dataSize3 = 0;
				for (int i3 = 0; i3 < this.mapValue_.size(); i3++) {
					dataSize3 += CodedOutputStream.computeInt32SizeNoTag(this.mapValue_.get(i3).intValue());
				}
				size = size + dataSize3 + (getMapValueList().size() * 1);
			}
			if ((this.bitField0_ & 1) == 1) {
				size += CodedOutputStream.computeInt32Size(4, this.macroReference_);
			}
			if (this.templateToken_ != null && this.templateToken_.size() > 0) {
				int dataSize4 = 0;
				for (int i4 = 0; i4 < this.templateToken_.size(); i4++) {
					dataSize4 += CodedOutputStream.computeInt32SizeNoTag(this.templateToken_.get(i4).intValue());
				}
				size = size + dataSize4 + (getTemplateTokenList().size() * 1);
			}
			if ((this.bitField0_ & 2) == 2) {
				size += CodedOutputStream.computeInt32Size(6, this.macroNameReference_);
			}
			int size2 = size + this.unknownFields.size();
			this.cachedSize = size2;
			return size2;
		}

		/* access modifiers changed from: protected */
		public Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public ServingValue clear() {
			assertMutable();
			super.clear();
			this.listItem_ = null;
			this.mapKey_ = null;
			this.mapValue_ = null;
			this.macroReference_ = 0;
			this.bitField0_ &= -2;
			this.templateToken_ = null;
			this.macroNameReference_ = 0;
			this.bitField0_ &= -3;
			return this;
		}

		public boolean equals(Object obj) {
			boolean result;
			boolean result2;
			boolean result3;
			boolean result4;
			boolean result5;
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof ServingValue)) {
				return super.equals(obj);
			}
			ServingValue other = (ServingValue) obj;
			if (!(1 != 0 && getListItemList().equals(other.getListItemList())) || !getMapKeyList().equals(other.getMapKeyList())) {
				result = false;
			} else {
				result = true;
			}
			if (!result || !getMapValueList().equals(other.getMapValueList())) {
				result2 = false;
			} else {
				result2 = true;
			}
			if (!result2 || hasMacroReference() != other.hasMacroReference()) {
				result3 = false;
			} else {
				result3 = true;
			}
			if (hasMacroReference()) {
				if (!result3 || getMacroReference() != other.getMacroReference()) {
					result3 = false;
				} else {
					result3 = true;
				}
			}
			if (!result3 || !getTemplateTokenList().equals(other.getTemplateTokenList())) {
				result4 = false;
			} else {
				result4 = true;
			}
			if (!result4 || hasMacroNameReference() != other.hasMacroNameReference()) {
				result5 = false;
			} else {
				result5 = true;
			}
			if (hasMacroNameReference()) {
				if (!result5 || getMacroNameReference() != other.getMacroNameReference()) {
					result5 = false;
				} else {
					result5 = true;
				}
			}
			return result5;
		}

		public int hashCode() {
			int hash = 41;
			if (getListItemCount() > 0) {
				int hash2 = 1517 + 1;
				hash = 80454 + getListItemList().hashCode();
			}
			if (getMapKeyCount() > 0) {
				hash = (((hash * 37) + 2) * 53) + getMapKeyList().hashCode();
			}
			if (getMapValueCount() > 0) {
				hash = (((hash * 37) + 3) * 53) + getMapValueList().hashCode();
			}
			if (hasMacroReference()) {
				hash = (((hash * 37) + 4) * 53) + getMacroReference();
			}
			if (getTemplateTokenCount() > 0) {
				hash = (((hash * 37) + 5) * 53) + getTemplateTokenList().hashCode();
			}
			if (hasMacroNameReference()) {
				hash = (((hash * 37) + 6) * 53) + getMacroNameReference();
			}
			return (hash * 29) + this.unknownFields.hashCode();
		}

		static {
			defaultInstance.initFields();
			defaultInstance.makeImmutable();
		}

		/* access modifiers changed from: protected */
		public MessageLite internalImmutableDefault() {
			if (immutableDefault == null) {
				immutableDefault = internalImmutableDefault("com.google.analytics.containertag.proto.Serving$ServingValue");
			}
			return immutableDefault;
		}
	}

	public static final class Property extends GeneratedMutableMessageLite<Property> implements MutableMessageLite {
		public static final int KEY_FIELD_NUMBER = 1;
		public static final int VALUE_FIELD_NUMBER = 2;
		private static final Property defaultInstance = new Property(true);
		public static Parser<Property> PARSER = AbstractMutableMessageLite.internalNewParserForType(defaultInstance);
		private static volatile MessageLite immutableDefault = null;
		private static final long serialVersionUID = 0;
		private int bitField0_;
		private int key_;
		private int value_;

		private Property() {
			initFields();
		}

		private Property(boolean noInit) {
		}

		public Property newMessageForType() {
			return new Property();
		}

		public static Property newMessage() {
			return new Property();
		}

		private void initFields() {
		}

		public static Property getDefaultInstance() {
			return defaultInstance;
		}

		public final Property getDefaultInstanceForType() {
			return defaultInstance;
		}

		public Parser<Property> getParserForType() {
			return PARSER;
		}

		public boolean hasKey() {
			return (this.bitField0_ & 1) == 1;
		}

		public int getKey() {
			return this.key_;
		}

		public Property setKey(int value) {
			assertMutable();
			this.bitField0_ |= 1;
			this.key_ = value;
			return this;
		}

		public Property clearKey() {
			assertMutable();
			this.bitField0_ &= -2;
			this.key_ = 0;
			return this;
		}

		public boolean hasValue() {
			return (this.bitField0_ & 2) == 2;
		}

		public int getValue() {
			return this.value_;
		}

		public Property setValue(int value) {
			assertMutable();
			this.bitField0_ |= 2;
			this.value_ = value;
			return this;
		}

		public Property clearValue() {
			assertMutable();
			this.bitField0_ &= -3;
			this.value_ = 0;
			return this;
		}

		public final boolean isInitialized() {
			if (hasKey() && hasValue()) {
				return true;
			}
			return false;
		}

		public Property clone() {
			return newMessageForType().mergeFrom(this);
		}

		public Property mergeFrom(Property other) {
			if (this == other) {
				throw new IllegalArgumentException("mergeFrom(message) called on the same message.");
			}
			assertMutable();
			if (other != getDefaultInstance()) {
				if (other.hasKey()) {
					setKey(other.getKey());
				}
				if (other.hasValue()) {
					setValue(other.getValue());
				}
				this.unknownFields = this.unknownFields.concat(other.unknownFields);
			}
			return this;
		}

		public boolean mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
			assertMutable();
			try {
				ByteString.Output unknownFieldsOutput = ByteString.newOutput();
				CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance((OutputStream) unknownFieldsOutput);
				boolean done = false;
				while (!done) {
					int tag = input.readTag();
					switch (tag) {
						case 0:
							done = true;
							break;
						case 8:
							this.bitField0_ |= 1;
							this.key_ = input.readInt32();
							break;
						case 16:
							this.bitField0_ |= 2;
							this.value_ = input.readInt32();
							break;
						default:
							if (parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
								break;
							} else {
								done = true;
								break;
							}
					}
				}
				unknownFieldsCodedOutput.flush();
				this.unknownFields = unknownFieldsOutput.toByteString();
				return true;
			} catch (IOException e) {
				return false;
			}
		}

		public void writeToWithCachedSizes(CodedOutputStream output) throws IOException {
			int bytesWrittenBefore = output.getTotalBytesWritten();
			output.writeInt32(1, this.key_);
			output.writeInt32(2, this.value_);
			output.writeRawBytes(this.unknownFields);
			if (getCachedSize() != output.getTotalBytesWritten() - bytesWrittenBefore) {
				throw new RuntimeException("Serialized size doesn't match cached size. You may forget to call getSerializedSize() or the message is being modified concurrently.");
			}
		}

		public int getSerializedSize() {
			int size = 0 + CodedOutputStream.computeInt32Size(1, this.key_) + CodedOutputStream.computeInt32Size(2, this.value_) + this.unknownFields.size();
			this.cachedSize = size;
			return size;
		}

		/* access modifiers changed from: protected */
		public Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public Property clear() {
			assertMutable();
			super.clear();
			this.key_ = 0;
			this.bitField0_ &= -2;
			this.value_ = 0;
			this.bitField0_ &= -3;
			return this;
		}

		public boolean equals(Object obj) {
			boolean result;
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof Property)) {
				return super.equals(obj);
			}
			Property other = (Property) obj;
			boolean result2 = 1 != 0 && hasKey() == other.hasKey();
			if (hasKey()) {
				result2 = result2 && getKey() == other.getKey();
			}
			if (!result2 || hasValue() != other.hasValue()) {
				result = false;
			} else {
				result = true;
			}
			if (hasValue()) {
				if (!result || getValue() != other.getValue()) {
					result = false;
				} else {
					result = true;
				}
			}
			return result;
		}

		public int hashCode() {
			int hash = 41;
			if (hasKey()) {
				int hash2 = 1517 + 1;
				hash = 80454 + getKey();
			}
			if (hasValue()) {
				hash = (((hash * 37) + 2) * 53) + getValue();
			}
			return (hash * 29) + this.unknownFields.hashCode();
		}

		static {
			defaultInstance.initFields();
			defaultInstance.makeImmutable();
		}

		/* access modifiers changed from: protected */
		public MessageLite internalImmutableDefault() {
			if (immutableDefault == null) {
				immutableDefault = internalImmutableDefault("com.google.analytics.containertag.proto.Serving$Property");
			}
			return immutableDefault;
		}
	}

	public static final class FunctionCall extends GeneratedMutableMessageLite<FunctionCall> implements MutableMessageLite {
		public static final int FUNCTION_FIELD_NUMBER = 2;
		public static final int LIVE_ONLY_FIELD_NUMBER = 6;
		public static final int NAME_FIELD_NUMBER = 4;
		public static final int PROPERTY_FIELD_NUMBER = 3;
		public static final int SERVER_SIDE_FIELD_NUMBER = 1;
		private static final FunctionCall defaultInstance = new FunctionCall(true);
		public static Parser<FunctionCall> PARSER = AbstractMutableMessageLite.internalNewParserForType(defaultInstance);
		private static volatile MessageLite immutableDefault = null;
		private static final long serialVersionUID = 0;
		private int bitField0_;
		private int function_;
		private boolean liveOnly_;
		private int name_;
		private List<Integer> property_ = null;
		private boolean serverSide_;

		private FunctionCall() {
			initFields();
		}

		private FunctionCall(boolean noInit) {
		}

		public FunctionCall newMessageForType() {
			return new FunctionCall();
		}

		public static FunctionCall newMessage() {
			return new FunctionCall();
		}

		private void initFields() {
		}

		public static FunctionCall getDefaultInstance() {
			return defaultInstance;
		}

		public final FunctionCall getDefaultInstanceForType() {
			return defaultInstance;
		}

		public Parser<FunctionCall> getParserForType() {
			return PARSER;
		}

		private void ensurePropertyInitialized() {
			if (this.property_ == null) {
				this.property_ = new ArrayList();
			}
		}

		public List<Integer> getPropertyList() {
			if (this.property_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.property_);
		}

		public List<Integer> getMutablePropertyList() {
			assertMutable();
			ensurePropertyInitialized();
			return this.property_;
		}

		public int getPropertyCount() {
			if (this.property_ == null) {
				return 0;
			}
			return this.property_.size();
		}

		public int getProperty(int index) {
			return this.property_.get(index).intValue();
		}

		public FunctionCall setProperty(int index, int value) {
			assertMutable();
			ensurePropertyInitialized();
			this.property_.set(index, Integer.valueOf(value));
			return this;
		}

		public FunctionCall addProperty(int value) {
			assertMutable();
			ensurePropertyInitialized();
			this.property_.add(Integer.valueOf(value));
			return this;
		}

		public FunctionCall addAllProperty(Iterable<? extends Integer> values) {
			assertMutable();
			ensurePropertyInitialized();
			AbstractMutableMessageLite.addAll(values, this.property_);
			return this;
		}

		public FunctionCall clearProperty() {
			assertMutable();
			this.property_ = null;
			return this;
		}

		public boolean hasFunction() {
			return (this.bitField0_ & 1) == 1;
		}

		public int getFunction() {
			return this.function_;
		}

		public FunctionCall setFunction(int value) {
			assertMutable();
			this.bitField0_ |= 1;
			this.function_ = value;
			return this;
		}

		public FunctionCall clearFunction() {
			assertMutable();
			this.bitField0_ &= -2;
			this.function_ = 0;
			return this;
		}

		public boolean hasName() {
			return (this.bitField0_ & 2) == 2;
		}

		public int getName() {
			return this.name_;
		}

		public FunctionCall setName(int value) {
			assertMutable();
			this.bitField0_ |= 2;
			this.name_ = value;
			return this;
		}

		public FunctionCall clearName() {
			assertMutable();
			this.bitField0_ &= -3;
			this.name_ = 0;
			return this;
		}

		public boolean hasLiveOnly() {
			return (this.bitField0_ & 4) == 4;
		}

		public boolean getLiveOnly() {
			return this.liveOnly_;
		}

		public FunctionCall setLiveOnly(boolean value) {
			assertMutable();
			this.bitField0_ |= 4;
			this.liveOnly_ = value;
			return this;
		}

		public FunctionCall clearLiveOnly() {
			assertMutable();
			this.bitField0_ &= -5;
			this.liveOnly_ = false;
			return this;
		}

		public boolean hasServerSide() {
			return (this.bitField0_ & 8) == 8;
		}

		public boolean getServerSide() {
			return this.serverSide_;
		}

		public FunctionCall setServerSide(boolean value) {
			assertMutable();
			this.bitField0_ |= 8;
			this.serverSide_ = value;
			return this;
		}

		public FunctionCall clearServerSide() {
			assertMutable();
			this.bitField0_ &= -9;
			this.serverSide_ = false;
			return this;
		}

		public final boolean isInitialized() {
			if (!hasFunction()) {
				return false;
			}
			return true;
		}

		public FunctionCall clone() {
			return newMessageForType().mergeFrom(this);
		}

		public FunctionCall mergeFrom(FunctionCall other) {
			if (this == other) {
				throw new IllegalArgumentException("mergeFrom(message) called on the same message.");
			}
			assertMutable();
			if (other != getDefaultInstance()) {
				if (other.hasServerSide()) {
					setServerSide(other.getServerSide());
				}
				if (other.hasFunction()) {
					setFunction(other.getFunction());
				}
				if (other.property_ != null && !other.property_.isEmpty()) {
					ensurePropertyInitialized();
					this.property_.addAll(other.property_);
				}
				if (other.hasName()) {
					setName(other.getName());
				}
				if (other.hasLiveOnly()) {
					setLiveOnly(other.getLiveOnly());
				}
				this.unknownFields = this.unknownFields.concat(other.unknownFields);
			}
			return this;
		}

		public boolean mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
			assertMutable();
			try {
				ByteString.Output unknownFieldsOutput = ByteString.newOutput();
				CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance((OutputStream) unknownFieldsOutput);
				boolean done = false;
				while (!done) {
					int tag = input.readTag();
					switch (tag) {
						case 0:
							done = true;
							break;
						case 8:
							this.bitField0_ |= 8;
							this.serverSide_ = input.readBool();
							break;
						case 16:
							this.bitField0_ |= 1;
							this.function_ = input.readInt32();
							break;
						case 24:
							if (this.property_ == null) {
								this.property_ = new ArrayList();
							}
							this.property_.add(Integer.valueOf(input.readInt32()));
							break;
						case 26:
							int limit = input.pushLimit(input.readRawVarint32());
							if (this.property_ == null) {
								this.property_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.property_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit);
							break;
						case 32:
							this.bitField0_ |= 2;
							this.name_ = input.readInt32();
							break;
						case DateTimeParserConstants.ANY /*48*/:
							this.bitField0_ |= 4;
							this.liveOnly_ = input.readBool();
							break;
						default:
							if (parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
								break;
							} else {
								done = true;
								break;
							}
					}
				}
				unknownFieldsCodedOutput.flush();
				this.unknownFields = unknownFieldsOutput.toByteString();
				return true;
			} catch (IOException e) {
				return false;
			}
		}

		public void writeToWithCachedSizes(CodedOutputStream output) throws IOException {
			int bytesWrittenBefore = output.getTotalBytesWritten();
			if ((this.bitField0_ & 8) == 8) {
				output.writeBool(1, this.serverSide_);
			}
			output.writeInt32(2, this.function_);
			if (this.property_ != null) {
				for (int i = 0; i < this.property_.size(); i++) {
					output.writeInt32(3, this.property_.get(i).intValue());
				}
			}
			if ((this.bitField0_ & 2) == 2) {
				output.writeInt32(4, this.name_);
			}
			if ((this.bitField0_ & 4) == 4) {
				output.writeBool(6, this.liveOnly_);
			}
			output.writeRawBytes(this.unknownFields);
			if (getCachedSize() != output.getTotalBytesWritten() - bytesWrittenBefore) {
				throw new RuntimeException("Serialized size doesn't match cached size. You may forget to call getSerializedSize() or the message is being modified concurrently.");
			}
		}

		public int getSerializedSize() {
			int size = 0;
			if (this.property_ != null && this.property_.size() > 0) {
				int dataSize = 0;
				for (int i = 0; i < this.property_.size(); i++) {
					dataSize += CodedOutputStream.computeInt32SizeNoTag(this.property_.get(i).intValue());
				}
				size = 0 + dataSize + (getPropertyList().size() * 1);
			}
			int size2 = size + CodedOutputStream.computeInt32Size(2, this.function_);
			if ((this.bitField0_ & 2) == 2) {
				size2 += CodedOutputStream.computeInt32Size(4, this.name_);
			}
			if ((this.bitField0_ & 4) == 4) {
				size2 += CodedOutputStream.computeBoolSize(6, this.liveOnly_);
			}
			if ((this.bitField0_ & 8) == 8) {
				size2 += CodedOutputStream.computeBoolSize(1, this.serverSide_);
			}
			int size3 = size2 + this.unknownFields.size();
			this.cachedSize = size3;
			return size3;
		}

		/* access modifiers changed from: protected */
		public Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public FunctionCall clear() {
			assertMutable();
			super.clear();
			this.property_ = null;
			this.function_ = 0;
			this.bitField0_ &= -2;
			this.name_ = 0;
			this.bitField0_ &= -3;
			this.liveOnly_ = false;
			this.bitField0_ &= -5;
			this.serverSide_ = false;
			this.bitField0_ &= -9;
			return this;
		}

		public boolean equals(Object obj) {
			boolean result;
			boolean result2;
			boolean result3;
			boolean result4;
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof FunctionCall)) {
				return super.equals(obj);
			}
			FunctionCall other = (FunctionCall) obj;
			if (!(1 != 0 && getPropertyList().equals(other.getPropertyList())) || hasFunction() != other.hasFunction()) {
				result = false;
			} else {
				result = true;
			}
			if (hasFunction()) {
				if (!result || getFunction() != other.getFunction()) {
					result = false;
				} else {
					result = true;
				}
			}
			if (!result || hasName() != other.hasName()) {
				result2 = false;
			} else {
				result2 = true;
			}
			if (hasName()) {
				if (!result2 || getName() != other.getName()) {
					result2 = false;
				} else {
					result2 = true;
				}
			}
			if (!result2 || hasLiveOnly() != other.hasLiveOnly()) {
				result3 = false;
			} else {
				result3 = true;
			}
			if (hasLiveOnly()) {
				if (!result3 || getLiveOnly() != other.getLiveOnly()) {
					result3 = false;
				} else {
					result3 = true;
				}
			}
			if (!result3 || hasServerSide() != other.hasServerSide()) {
				result4 = false;
			} else {
				result4 = true;
			}
			if (hasServerSide()) {
				if (!result4 || getServerSide() != other.getServerSide()) {
					result4 = false;
				} else {
					result4 = true;
				}
			}
			return result4;
		}

		public int hashCode() {
			int hash = 41;
			if (getPropertyCount() > 0) {
				int hash2 = 1517 + 3;
				hash = 80560 + getPropertyList().hashCode();
			}
			if (hasFunction()) {
				hash = (((hash * 37) + 2) * 53) + getFunction();
			}
			if (hasName()) {
				hash = (((hash * 37) + 4) * 53) + getName();
			}
			if (hasLiveOnly()) {
				hash = (((hash * 37) + 6) * 53) + Internal.hashBoolean(getLiveOnly());
			}
			if (hasServerSide()) {
				hash = (((hash * 37) + 1) * 53) + Internal.hashBoolean(getServerSide());
			}
			return (hash * 29) + this.unknownFields.hashCode();
		}

		static {
			defaultInstance.initFields();
			defaultInstance.makeImmutable();
		}

		/* access modifiers changed from: protected */
		public MessageLite internalImmutableDefault() {
			if (immutableDefault == null) {
				immutableDefault = internalImmutableDefault("com.google.analytics.containertag.proto.Serving$FunctionCall");
			}
			return immutableDefault;
		}
	}

	public static final class Rule extends GeneratedMutableMessageLite<Rule> implements MutableMessageLite {
		public static final int ADD_MACRO_FIELD_NUMBER = 7;
		public static final int ADD_MACRO_RULE_NAME_FIELD_NUMBER = 9;
		public static final int ADD_TAG_FIELD_NUMBER = 3;
		public static final int ADD_TAG_RULE_NAME_FIELD_NUMBER = 5;
		public static final int NEGATIVE_PREDICATE_FIELD_NUMBER = 2;
		public static final int POSITIVE_PREDICATE_FIELD_NUMBER = 1;
		public static final int REMOVE_MACRO_FIELD_NUMBER = 8;
		public static final int REMOVE_MACRO_RULE_NAME_FIELD_NUMBER = 10;
		public static final int REMOVE_TAG_FIELD_NUMBER = 4;
		public static final int REMOVE_TAG_RULE_NAME_FIELD_NUMBER = 6;
		private static final Rule defaultInstance = new Rule(true);
		public static Parser<Rule> PARSER = AbstractMutableMessageLite.internalNewParserForType(defaultInstance);
		private static volatile MessageLite immutableDefault = null;
		private static final long serialVersionUID = 0;
		private List<Integer> addMacroRuleName_ = null;
		private List<Integer> addMacro_ = null;
		private List<Integer> addTagRuleName_ = null;
		private List<Integer> addTag_ = null;
		private List<Integer> negativePredicate_ = null;
		private List<Integer> positivePredicate_ = null;
		private List<Integer> removeMacroRuleName_ = null;
		private List<Integer> removeMacro_ = null;
		private List<Integer> removeTagRuleName_ = null;
		private List<Integer> removeTag_ = null;

		private Rule() {
			initFields();
		}

		private Rule(boolean noInit) {
		}

		public Rule newMessageForType() {
			return new Rule();
		}

		public static Rule newMessage() {
			return new Rule();
		}

		private void initFields() {
		}

		public static Rule getDefaultInstance() {
			return defaultInstance;
		}

		public final Rule getDefaultInstanceForType() {
			return defaultInstance;
		}

		public Parser<Rule> getParserForType() {
			return PARSER;
		}

		private void ensurePositivePredicateInitialized() {
			if (this.positivePredicate_ == null) {
				this.positivePredicate_ = new ArrayList();
			}
		}

		public List<Integer> getPositivePredicateList() {
			if (this.positivePredicate_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.positivePredicate_);
		}

		public List<Integer> getMutablePositivePredicateList() {
			assertMutable();
			ensurePositivePredicateInitialized();
			return this.positivePredicate_;
		}

		public int getPositivePredicateCount() {
			if (this.positivePredicate_ == null) {
				return 0;
			}
			return this.positivePredicate_.size();
		}

		public int getPositivePredicate(int index) {
			return this.positivePredicate_.get(index).intValue();
		}

		public Rule setPositivePredicate(int index, int value) {
			assertMutable();
			ensurePositivePredicateInitialized();
			this.positivePredicate_.set(index, Integer.valueOf(value));
			return this;
		}

		public Rule addPositivePredicate(int value) {
			assertMutable();
			ensurePositivePredicateInitialized();
			this.positivePredicate_.add(Integer.valueOf(value));
			return this;
		}

		public Rule addAllPositivePredicate(Iterable<? extends Integer> values) {
			assertMutable();
			ensurePositivePredicateInitialized();
			AbstractMutableMessageLite.addAll(values, this.positivePredicate_);
			return this;
		}

		public Rule clearPositivePredicate() {
			assertMutable();
			this.positivePredicate_ = null;
			return this;
		}

		private void ensureNegativePredicateInitialized() {
			if (this.negativePredicate_ == null) {
				this.negativePredicate_ = new ArrayList();
			}
		}

		public List<Integer> getNegativePredicateList() {
			if (this.negativePredicate_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.negativePredicate_);
		}

		public List<Integer> getMutableNegativePredicateList() {
			assertMutable();
			ensureNegativePredicateInitialized();
			return this.negativePredicate_;
		}

		public int getNegativePredicateCount() {
			if (this.negativePredicate_ == null) {
				return 0;
			}
			return this.negativePredicate_.size();
		}

		public int getNegativePredicate(int index) {
			return this.negativePredicate_.get(index).intValue();
		}

		public Rule setNegativePredicate(int index, int value) {
			assertMutable();
			ensureNegativePredicateInitialized();
			this.negativePredicate_.set(index, Integer.valueOf(value));
			return this;
		}

		public Rule addNegativePredicate(int value) {
			assertMutable();
			ensureNegativePredicateInitialized();
			this.negativePredicate_.add(Integer.valueOf(value));
			return this;
		}

		public Rule addAllNegativePredicate(Iterable<? extends Integer> values) {
			assertMutable();
			ensureNegativePredicateInitialized();
			AbstractMutableMessageLite.addAll(values, this.negativePredicate_);
			return this;
		}

		public Rule clearNegativePredicate() {
			assertMutable();
			this.negativePredicate_ = null;
			return this;
		}

		private void ensureAddTagInitialized() {
			if (this.addTag_ == null) {
				this.addTag_ = new ArrayList();
			}
		}

		public List<Integer> getAddTagList() {
			if (this.addTag_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.addTag_);
		}

		public List<Integer> getMutableAddTagList() {
			assertMutable();
			ensureAddTagInitialized();
			return this.addTag_;
		}

		public int getAddTagCount() {
			if (this.addTag_ == null) {
				return 0;
			}
			return this.addTag_.size();
		}

		public int getAddTag(int index) {
			return this.addTag_.get(index).intValue();
		}

		public Rule setAddTag(int index, int value) {
			assertMutable();
			ensureAddTagInitialized();
			this.addTag_.set(index, Integer.valueOf(value));
			return this;
		}

		public Rule addAddTag(int value) {
			assertMutable();
			ensureAddTagInitialized();
			this.addTag_.add(Integer.valueOf(value));
			return this;
		}

		public Rule addAllAddTag(Iterable<? extends Integer> values) {
			assertMutable();
			ensureAddTagInitialized();
			AbstractMutableMessageLite.addAll(values, this.addTag_);
			return this;
		}

		public Rule clearAddTag() {
			assertMutable();
			this.addTag_ = null;
			return this;
		}

		private void ensureRemoveTagInitialized() {
			if (this.removeTag_ == null) {
				this.removeTag_ = new ArrayList();
			}
		}

		public List<Integer> getRemoveTagList() {
			if (this.removeTag_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.removeTag_);
		}

		public List<Integer> getMutableRemoveTagList() {
			assertMutable();
			ensureRemoveTagInitialized();
			return this.removeTag_;
		}

		public int getRemoveTagCount() {
			if (this.removeTag_ == null) {
				return 0;
			}
			return this.removeTag_.size();
		}

		public int getRemoveTag(int index) {
			return this.removeTag_.get(index).intValue();
		}

		public Rule setRemoveTag(int index, int value) {
			assertMutable();
			ensureRemoveTagInitialized();
			this.removeTag_.set(index, Integer.valueOf(value));
			return this;
		}

		public Rule addRemoveTag(int value) {
			assertMutable();
			ensureRemoveTagInitialized();
			this.removeTag_.add(Integer.valueOf(value));
			return this;
		}

		public Rule addAllRemoveTag(Iterable<? extends Integer> values) {
			assertMutable();
			ensureRemoveTagInitialized();
			AbstractMutableMessageLite.addAll(values, this.removeTag_);
			return this;
		}

		public Rule clearRemoveTag() {
			assertMutable();
			this.removeTag_ = null;
			return this;
		}

		private void ensureAddTagRuleNameInitialized() {
			if (this.addTagRuleName_ == null) {
				this.addTagRuleName_ = new ArrayList();
			}
		}

		public List<Integer> getAddTagRuleNameList() {
			if (this.addTagRuleName_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.addTagRuleName_);
		}

		public List<Integer> getMutableAddTagRuleNameList() {
			assertMutable();
			ensureAddTagRuleNameInitialized();
			return this.addTagRuleName_;
		}

		public int getAddTagRuleNameCount() {
			if (this.addTagRuleName_ == null) {
				return 0;
			}
			return this.addTagRuleName_.size();
		}

		public int getAddTagRuleName(int index) {
			return this.addTagRuleName_.get(index).intValue();
		}

		public Rule setAddTagRuleName(int index, int value) {
			assertMutable();
			ensureAddTagRuleNameInitialized();
			this.addTagRuleName_.set(index, Integer.valueOf(value));
			return this;
		}

		public Rule addAddTagRuleName(int value) {
			assertMutable();
			ensureAddTagRuleNameInitialized();
			this.addTagRuleName_.add(Integer.valueOf(value));
			return this;
		}

		public Rule addAllAddTagRuleName(Iterable<? extends Integer> values) {
			assertMutable();
			ensureAddTagRuleNameInitialized();
			AbstractMutableMessageLite.addAll(values, this.addTagRuleName_);
			return this;
		}

		public Rule clearAddTagRuleName() {
			assertMutable();
			this.addTagRuleName_ = null;
			return this;
		}

		private void ensureRemoveTagRuleNameInitialized() {
			if (this.removeTagRuleName_ == null) {
				this.removeTagRuleName_ = new ArrayList();
			}
		}

		public List<Integer> getRemoveTagRuleNameList() {
			if (this.removeTagRuleName_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.removeTagRuleName_);
		}

		public List<Integer> getMutableRemoveTagRuleNameList() {
			assertMutable();
			ensureRemoveTagRuleNameInitialized();
			return this.removeTagRuleName_;
		}

		public int getRemoveTagRuleNameCount() {
			if (this.removeTagRuleName_ == null) {
				return 0;
			}
			return this.removeTagRuleName_.size();
		}

		public int getRemoveTagRuleName(int index) {
			return this.removeTagRuleName_.get(index).intValue();
		}

		public Rule setRemoveTagRuleName(int index, int value) {
			assertMutable();
			ensureRemoveTagRuleNameInitialized();
			this.removeTagRuleName_.set(index, Integer.valueOf(value));
			return this;
		}

		public Rule addRemoveTagRuleName(int value) {
			assertMutable();
			ensureRemoveTagRuleNameInitialized();
			this.removeTagRuleName_.add(Integer.valueOf(value));
			return this;
		}

		public Rule addAllRemoveTagRuleName(Iterable<? extends Integer> values) {
			assertMutable();
			ensureRemoveTagRuleNameInitialized();
			AbstractMutableMessageLite.addAll(values, this.removeTagRuleName_);
			return this;
		}

		public Rule clearRemoveTagRuleName() {
			assertMutable();
			this.removeTagRuleName_ = null;
			return this;
		}

		private void ensureAddMacroInitialized() {
			if (this.addMacro_ == null) {
				this.addMacro_ = new ArrayList();
			}
		}

		public List<Integer> getAddMacroList() {
			if (this.addMacro_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.addMacro_);
		}

		public List<Integer> getMutableAddMacroList() {
			assertMutable();
			ensureAddMacroInitialized();
			return this.addMacro_;
		}

		public int getAddMacroCount() {
			if (this.addMacro_ == null) {
				return 0;
			}
			return this.addMacro_.size();
		}

		public int getAddMacro(int index) {
			return this.addMacro_.get(index).intValue();
		}

		public Rule setAddMacro(int index, int value) {
			assertMutable();
			ensureAddMacroInitialized();
			this.addMacro_.set(index, Integer.valueOf(value));
			return this;
		}

		public Rule addAddMacro(int value) {
			assertMutable();
			ensureAddMacroInitialized();
			this.addMacro_.add(Integer.valueOf(value));
			return this;
		}

		public Rule addAllAddMacro(Iterable<? extends Integer> values) {
			assertMutable();
			ensureAddMacroInitialized();
			AbstractMutableMessageLite.addAll(values, this.addMacro_);
			return this;
		}

		public Rule clearAddMacro() {
			assertMutable();
			this.addMacro_ = null;
			return this;
		}

		private void ensureRemoveMacroInitialized() {
			if (this.removeMacro_ == null) {
				this.removeMacro_ = new ArrayList();
			}
		}

		public List<Integer> getRemoveMacroList() {
			if (this.removeMacro_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.removeMacro_);
		}

		public List<Integer> getMutableRemoveMacroList() {
			assertMutable();
			ensureRemoveMacroInitialized();
			return this.removeMacro_;
		}

		public int getRemoveMacroCount() {
			if (this.removeMacro_ == null) {
				return 0;
			}
			return this.removeMacro_.size();
		}

		public int getRemoveMacro(int index) {
			return this.removeMacro_.get(index).intValue();
		}

		public Rule setRemoveMacro(int index, int value) {
			assertMutable();
			ensureRemoveMacroInitialized();
			this.removeMacro_.set(index, Integer.valueOf(value));
			return this;
		}

		public Rule addRemoveMacro(int value) {
			assertMutable();
			ensureRemoveMacroInitialized();
			this.removeMacro_.add(Integer.valueOf(value));
			return this;
		}

		public Rule addAllRemoveMacro(Iterable<? extends Integer> values) {
			assertMutable();
			ensureRemoveMacroInitialized();
			AbstractMutableMessageLite.addAll(values, this.removeMacro_);
			return this;
		}

		public Rule clearRemoveMacro() {
			assertMutable();
			this.removeMacro_ = null;
			return this;
		}

		private void ensureAddMacroRuleNameInitialized() {
			if (this.addMacroRuleName_ == null) {
				this.addMacroRuleName_ = new ArrayList();
			}
		}

		public List<Integer> getAddMacroRuleNameList() {
			if (this.addMacroRuleName_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.addMacroRuleName_);
		}

		public List<Integer> getMutableAddMacroRuleNameList() {
			assertMutable();
			ensureAddMacroRuleNameInitialized();
			return this.addMacroRuleName_;
		}

		public int getAddMacroRuleNameCount() {
			if (this.addMacroRuleName_ == null) {
				return 0;
			}
			return this.addMacroRuleName_.size();
		}

		public int getAddMacroRuleName(int index) {
			return this.addMacroRuleName_.get(index).intValue();
		}

		public Rule setAddMacroRuleName(int index, int value) {
			assertMutable();
			ensureAddMacroRuleNameInitialized();
			this.addMacroRuleName_.set(index, Integer.valueOf(value));
			return this;
		}

		public Rule addAddMacroRuleName(int value) {
			assertMutable();
			ensureAddMacroRuleNameInitialized();
			this.addMacroRuleName_.add(Integer.valueOf(value));
			return this;
		}

		public Rule addAllAddMacroRuleName(Iterable<? extends Integer> values) {
			assertMutable();
			ensureAddMacroRuleNameInitialized();
			AbstractMutableMessageLite.addAll(values, this.addMacroRuleName_);
			return this;
		}

		public Rule clearAddMacroRuleName() {
			assertMutable();
			this.addMacroRuleName_ = null;
			return this;
		}

		private void ensureRemoveMacroRuleNameInitialized() {
			if (this.removeMacroRuleName_ == null) {
				this.removeMacroRuleName_ = new ArrayList();
			}
		}

		public List<Integer> getRemoveMacroRuleNameList() {
			if (this.removeMacroRuleName_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.removeMacroRuleName_);
		}

		public List<Integer> getMutableRemoveMacroRuleNameList() {
			assertMutable();
			ensureRemoveMacroRuleNameInitialized();
			return this.removeMacroRuleName_;
		}

		public int getRemoveMacroRuleNameCount() {
			if (this.removeMacroRuleName_ == null) {
				return 0;
			}
			return this.removeMacroRuleName_.size();
		}

		public int getRemoveMacroRuleName(int index) {
			return this.removeMacroRuleName_.get(index).intValue();
		}

		public Rule setRemoveMacroRuleName(int index, int value) {
			assertMutable();
			ensureRemoveMacroRuleNameInitialized();
			this.removeMacroRuleName_.set(index, Integer.valueOf(value));
			return this;
		}

		public Rule addRemoveMacroRuleName(int value) {
			assertMutable();
			ensureRemoveMacroRuleNameInitialized();
			this.removeMacroRuleName_.add(Integer.valueOf(value));
			return this;
		}

		public Rule addAllRemoveMacroRuleName(Iterable<? extends Integer> values) {
			assertMutable();
			ensureRemoveMacroRuleNameInitialized();
			AbstractMutableMessageLite.addAll(values, this.removeMacroRuleName_);
			return this;
		}

		public Rule clearRemoveMacroRuleName() {
			assertMutable();
			this.removeMacroRuleName_ = null;
			return this;
		}

		public final boolean isInitialized() {
			return true;
		}

		public Rule clone() {
			return newMessageForType().mergeFrom(this);
		}

		public Rule mergeFrom(Rule other) {
			if (this == other) {
				throw new IllegalArgumentException("mergeFrom(message) called on the same message.");
			}
			assertMutable();
			if (other != getDefaultInstance()) {
				if (other.positivePredicate_ != null && !other.positivePredicate_.isEmpty()) {
					ensurePositivePredicateInitialized();
					this.positivePredicate_.addAll(other.positivePredicate_);
				}
				if (other.negativePredicate_ != null && !other.negativePredicate_.isEmpty()) {
					ensureNegativePredicateInitialized();
					this.negativePredicate_.addAll(other.negativePredicate_);
				}
				if (other.addTag_ != null && !other.addTag_.isEmpty()) {
					ensureAddTagInitialized();
					this.addTag_.addAll(other.addTag_);
				}
				if (other.removeTag_ != null && !other.removeTag_.isEmpty()) {
					ensureRemoveTagInitialized();
					this.removeTag_.addAll(other.removeTag_);
				}
				if (other.addTagRuleName_ != null && !other.addTagRuleName_.isEmpty()) {
					ensureAddTagRuleNameInitialized();
					this.addTagRuleName_.addAll(other.addTagRuleName_);
				}
				if (other.removeTagRuleName_ != null && !other.removeTagRuleName_.isEmpty()) {
					ensureRemoveTagRuleNameInitialized();
					this.removeTagRuleName_.addAll(other.removeTagRuleName_);
				}
				if (other.addMacro_ != null && !other.addMacro_.isEmpty()) {
					ensureAddMacroInitialized();
					this.addMacro_.addAll(other.addMacro_);
				}
				if (other.removeMacro_ != null && !other.removeMacro_.isEmpty()) {
					ensureRemoveMacroInitialized();
					this.removeMacro_.addAll(other.removeMacro_);
				}
				if (other.addMacroRuleName_ != null && !other.addMacroRuleName_.isEmpty()) {
					ensureAddMacroRuleNameInitialized();
					this.addMacroRuleName_.addAll(other.addMacroRuleName_);
				}
				if (other.removeMacroRuleName_ != null && !other.removeMacroRuleName_.isEmpty()) {
					ensureRemoveMacroRuleNameInitialized();
					this.removeMacroRuleName_.addAll(other.removeMacroRuleName_);
				}
				this.unknownFields = this.unknownFields.concat(other.unknownFields);
			}
			return this;
		}

		public boolean mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
			assertMutable();
			try {
				ByteString.Output unknownFieldsOutput = ByteString.newOutput();
				CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance((OutputStream) unknownFieldsOutput);
				boolean done = false;
				while (!done) {
					int tag = input.readTag();
					switch (tag) {
						case 0:
							done = true;
							break;
						case 8:
							if (this.positivePredicate_ == null) {
								this.positivePredicate_ = new ArrayList();
							}
							this.positivePredicate_.add(Integer.valueOf(input.readInt32()));
							break;
						case 10:
							int limit = input.pushLimit(input.readRawVarint32());
							if (this.positivePredicate_ == null) {
								this.positivePredicate_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.positivePredicate_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit);
							break;
						case 16:
							if (this.negativePredicate_ == null) {
								this.negativePredicate_ = new ArrayList();
							}
							this.negativePredicate_.add(Integer.valueOf(input.readInt32()));
							break;
						case 18:
							int limit2 = input.pushLimit(input.readRawVarint32());
							if (this.negativePredicate_ == null) {
								this.negativePredicate_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.negativePredicate_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit2);
							break;
						case 24:
							if (this.addTag_ == null) {
								this.addTag_ = new ArrayList();
							}
							this.addTag_.add(Integer.valueOf(input.readInt32()));
							break;
						case 26:
							int limit3 = input.pushLimit(input.readRawVarint32());
							if (this.addTag_ == null) {
								this.addTag_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.addTag_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit3);
							break;
						case 32:
							if (this.removeTag_ == null) {
								this.removeTag_ = new ArrayList();
							}
							this.removeTag_.add(Integer.valueOf(input.readInt32()));
							break;
						case 34:
							int limit4 = input.pushLimit(input.readRawVarint32());
							if (this.removeTag_ == null) {
								this.removeTag_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.removeTag_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit4);
							break;
						case 40:
							if (this.addTagRuleName_ == null) {
								this.addTagRuleName_ = new ArrayList();
							}
							this.addTagRuleName_.add(Integer.valueOf(input.readInt32()));
							break;
						case 42:
							int limit5 = input.pushLimit(input.readRawVarint32());
							if (this.addTagRuleName_ == null) {
								this.addTagRuleName_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.addTagRuleName_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit5);
							break;
						case DateTimeParserConstants.ANY /*48*/:
							if (this.removeTagRuleName_ == null) {
								this.removeTagRuleName_ = new ArrayList();
							}
							this.removeTagRuleName_.add(Integer.valueOf(input.readInt32()));
							break;
						case 50:
							int limit6 = input.pushLimit(input.readRawVarint32());
							if (this.removeTagRuleName_ == null) {
								this.removeTagRuleName_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.removeTagRuleName_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit6);
							break;
						case 56:
							if (this.addMacro_ == null) {
								this.addMacro_ = new ArrayList();
							}
							this.addMacro_.add(Integer.valueOf(input.readInt32()));
							break;
						case 58:
							int limit7 = input.pushLimit(input.readRawVarint32());
							if (this.addMacro_ == null) {
								this.addMacro_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.addMacro_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit7);
							break;
						case AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS:
							if (this.removeMacro_ == null) {
								this.removeMacro_ = new ArrayList();
							}
							this.removeMacro_.add(Integer.valueOf(input.readInt32()));
							break;
						case 66:
							int limit8 = input.pushLimit(input.readRawVarint32());
							if (this.removeMacro_ == null) {
								this.removeMacro_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.removeMacro_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit8);
							break;
						case 72:
							if (this.addMacroRuleName_ == null) {
								this.addMacroRuleName_ = new ArrayList();
							}
							this.addMacroRuleName_.add(Integer.valueOf(input.readInt32()));
							break;
						case 74:
							int limit9 = input.pushLimit(input.readRawVarint32());
							if (this.addMacroRuleName_ == null) {
								this.addMacroRuleName_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.addMacroRuleName_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit9);
							break;
						case 80:
							if (this.removeMacroRuleName_ == null) {
								this.removeMacroRuleName_ = new ArrayList();
							}
							this.removeMacroRuleName_.add(Integer.valueOf(input.readInt32()));
							break;
						case 82:
							int limit10 = input.pushLimit(input.readRawVarint32());
							if (this.removeMacroRuleName_ == null) {
								this.removeMacroRuleName_ = new ArrayList();
							}
							while (input.getBytesUntilLimit() > 0) {
								this.removeMacroRuleName_.add(Integer.valueOf(input.readInt32()));
							}
							input.popLimit(limit10);
							break;
						default:
							if (parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
								break;
							} else {
								done = true;
								break;
							}
					}
				}
				unknownFieldsCodedOutput.flush();
				this.unknownFields = unknownFieldsOutput.toByteString();
				return true;
			} catch (IOException e) {
				return false;
			}
		}

		public void writeToWithCachedSizes(CodedOutputStream output) throws IOException {
			int bytesWrittenBefore = output.getTotalBytesWritten();
			if (this.positivePredicate_ != null) {
				for (int i = 0; i < this.positivePredicate_.size(); i++) {
					output.writeInt32(1, this.positivePredicate_.get(i).intValue());
				}
			}
			if (this.negativePredicate_ != null) {
				for (int i2 = 0; i2 < this.negativePredicate_.size(); i2++) {
					output.writeInt32(2, this.negativePredicate_.get(i2).intValue());
				}
			}
			if (this.addTag_ != null) {
				for (int i3 = 0; i3 < this.addTag_.size(); i3++) {
					output.writeInt32(3, this.addTag_.get(i3).intValue());
				}
			}
			if (this.removeTag_ != null) {
				for (int i4 = 0; i4 < this.removeTag_.size(); i4++) {
					output.writeInt32(4, this.removeTag_.get(i4).intValue());
				}
			}
			if (this.addTagRuleName_ != null) {
				for (int i5 = 0; i5 < this.addTagRuleName_.size(); i5++) {
					output.writeInt32(5, this.addTagRuleName_.get(i5).intValue());
				}
			}
			if (this.removeTagRuleName_ != null) {
				for (int i6 = 0; i6 < this.removeTagRuleName_.size(); i6++) {
					output.writeInt32(6, this.removeTagRuleName_.get(i6).intValue());
				}
			}
			if (this.addMacro_ != null) {
				for (int i7 = 0; i7 < this.addMacro_.size(); i7++) {
					output.writeInt32(7, this.addMacro_.get(i7).intValue());
				}
			}
			if (this.removeMacro_ != null) {
				for (int i8 = 0; i8 < this.removeMacro_.size(); i8++) {
					output.writeInt32(8, this.removeMacro_.get(i8).intValue());
				}
			}
			if (this.addMacroRuleName_ != null) {
				for (int i9 = 0; i9 < this.addMacroRuleName_.size(); i9++) {
					output.writeInt32(9, this.addMacroRuleName_.get(i9).intValue());
				}
			}
			if (this.removeMacroRuleName_ != null) {
				for (int i10 = 0; i10 < this.removeMacroRuleName_.size(); i10++) {
					output.writeInt32(10, this.removeMacroRuleName_.get(i10).intValue());
				}
			}
			output.writeRawBytes(this.unknownFields);
			if (getCachedSize() != output.getTotalBytesWritten() - bytesWrittenBefore) {
				throw new RuntimeException("Serialized size doesn't match cached size. You may forget to call getSerializedSize() or the message is being modified concurrently.");
			}
		}

		public int getSerializedSize() {
			int size = 0;
			if (this.positivePredicate_ != null && this.positivePredicate_.size() > 0) {
				int dataSize = 0;
				for (int i = 0; i < this.positivePredicate_.size(); i++) {
					dataSize += CodedOutputStream.computeInt32SizeNoTag(this.positivePredicate_.get(i).intValue());
				}
				size = 0 + dataSize + (getPositivePredicateList().size() * 1);
			}
			if (this.negativePredicate_ != null && this.negativePredicate_.size() > 0) {
				int dataSize2 = 0;
				for (int i2 = 0; i2 < this.negativePredicate_.size(); i2++) {
					dataSize2 += CodedOutputStream.computeInt32SizeNoTag(this.negativePredicate_.get(i2).intValue());
				}
				size = size + dataSize2 + (getNegativePredicateList().size() * 1);
			}
			if (this.addTag_ != null && this.addTag_.size() > 0) {
				int dataSize3 = 0;
				for (int i3 = 0; i3 < this.addTag_.size(); i3++) {
					dataSize3 += CodedOutputStream.computeInt32SizeNoTag(this.addTag_.get(i3).intValue());
				}
				size = size + dataSize3 + (getAddTagList().size() * 1);
			}
			if (this.removeTag_ != null && this.removeTag_.size() > 0) {
				int dataSize4 = 0;
				for (int i4 = 0; i4 < this.removeTag_.size(); i4++) {
					dataSize4 += CodedOutputStream.computeInt32SizeNoTag(this.removeTag_.get(i4).intValue());
				}
				size = size + dataSize4 + (getRemoveTagList().size() * 1);
			}
			if (this.addTagRuleName_ != null && this.addTagRuleName_.size() > 0) {
				int dataSize5 = 0;
				for (int i5 = 0; i5 < this.addTagRuleName_.size(); i5++) {
					dataSize5 += CodedOutputStream.computeInt32SizeNoTag(this.addTagRuleName_.get(i5).intValue());
				}
				size = size + dataSize5 + (getAddTagRuleNameList().size() * 1);
			}
			if (this.removeTagRuleName_ != null && this.removeTagRuleName_.size() > 0) {
				int dataSize6 = 0;
				for (int i6 = 0; i6 < this.removeTagRuleName_.size(); i6++) {
					dataSize6 += CodedOutputStream.computeInt32SizeNoTag(this.removeTagRuleName_.get(i6).intValue());
				}
				size = size + dataSize6 + (getRemoveTagRuleNameList().size() * 1);
			}
			if (this.addMacro_ != null && this.addMacro_.size() > 0) {
				int dataSize7 = 0;
				for (int i7 = 0; i7 < this.addMacro_.size(); i7++) {
					dataSize7 += CodedOutputStream.computeInt32SizeNoTag(this.addMacro_.get(i7).intValue());
				}
				size = size + dataSize7 + (getAddMacroList().size() * 1);
			}
			if (this.removeMacro_ != null && this.removeMacro_.size() > 0) {
				int dataSize8 = 0;
				for (int i8 = 0; i8 < this.removeMacro_.size(); i8++) {
					dataSize8 += CodedOutputStream.computeInt32SizeNoTag(this.removeMacro_.get(i8).intValue());
				}
				size = size + dataSize8 + (getRemoveMacroList().size() * 1);
			}
			if (this.addMacroRuleName_ != null && this.addMacroRuleName_.size() > 0) {
				int dataSize9 = 0;
				for (int i9 = 0; i9 < this.addMacroRuleName_.size(); i9++) {
					dataSize9 += CodedOutputStream.computeInt32SizeNoTag(this.addMacroRuleName_.get(i9).intValue());
				}
				size = size + dataSize9 + (getAddMacroRuleNameList().size() * 1);
			}
			if (this.removeMacroRuleName_ != null && this.removeMacroRuleName_.size() > 0) {
				int dataSize10 = 0;
				for (int i10 = 0; i10 < this.removeMacroRuleName_.size(); i10++) {
					dataSize10 += CodedOutputStream.computeInt32SizeNoTag(this.removeMacroRuleName_.get(i10).intValue());
				}
				size = size + dataSize10 + (getRemoveMacroRuleNameList().size() * 1);
			}
			int size2 = size + this.unknownFields.size();
			this.cachedSize = size2;
			return size2;
		}

		/* access modifiers changed from: protected */
		public Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public Rule clear() {
			assertMutable();
			super.clear();
			this.positivePredicate_ = null;
			this.negativePredicate_ = null;
			this.addTag_ = null;
			this.removeTag_ = null;
			this.addTagRuleName_ = null;
			this.removeTagRuleName_ = null;
			this.addMacro_ = null;
			this.removeMacro_ = null;
			this.addMacroRuleName_ = null;
			this.removeMacroRuleName_ = null;
			return this;
		}

		public boolean equals(Object obj) {
			boolean result;
			boolean result2;
			boolean result3;
			boolean result4;
			boolean result5;
			boolean result6;
			boolean result7;
			boolean result8;
			boolean result9;
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof Rule)) {
				return super.equals(obj);
			}
			Rule other = (Rule) obj;
			if (!(1 != 0 && getPositivePredicateList().equals(other.getPositivePredicateList())) || !getNegativePredicateList().equals(other.getNegativePredicateList())) {
				result = false;
			} else {
				result = true;
			}
			if (!result || !getAddTagList().equals(other.getAddTagList())) {
				result2 = false;
			} else {
				result2 = true;
			}
			if (!result2 || !getRemoveTagList().equals(other.getRemoveTagList())) {
				result3 = false;
			} else {
				result3 = true;
			}
			if (!result3 || !getAddTagRuleNameList().equals(other.getAddTagRuleNameList())) {
				result4 = false;
			} else {
				result4 = true;
			}
			if (!result4 || !getRemoveTagRuleNameList().equals(other.getRemoveTagRuleNameList())) {
				result5 = false;
			} else {
				result5 = true;
			}
			if (!result5 || !getAddMacroList().equals(other.getAddMacroList())) {
				result6 = false;
			} else {
				result6 = true;
			}
			if (!result6 || !getRemoveMacroList().equals(other.getRemoveMacroList())) {
				result7 = false;
			} else {
				result7 = true;
			}
			if (!result7 || !getAddMacroRuleNameList().equals(other.getAddMacroRuleNameList())) {
				result8 = false;
			} else {
				result8 = true;
			}
			if (!result8 || !getRemoveMacroRuleNameList().equals(other.getRemoveMacroRuleNameList())) {
				result9 = false;
			} else {
				result9 = true;
			}
			return result9;
		}

		public int hashCode() {
			int hash = 41;
			if (getPositivePredicateCount() > 0) {
				int hash2 = 1517 + 1;
				hash = 80454 + getPositivePredicateList().hashCode();
			}
			if (getNegativePredicateCount() > 0) {
				hash = (((hash * 37) + 2) * 53) + getNegativePredicateList().hashCode();
			}
			if (getAddTagCount() > 0) {
				hash = (((hash * 37) + 3) * 53) + getAddTagList().hashCode();
			}
			if (getRemoveTagCount() > 0) {
				hash = (((hash * 37) + 4) * 53) + getRemoveTagList().hashCode();
			}
			if (getAddTagRuleNameCount() > 0) {
				hash = (((hash * 37) + 5) * 53) + getAddTagRuleNameList().hashCode();
			}
			if (getRemoveTagRuleNameCount() > 0) {
				hash = (((hash * 37) + 6) * 53) + getRemoveTagRuleNameList().hashCode();
			}
			if (getAddMacroCount() > 0) {
				hash = (((hash * 37) + 7) * 53) + getAddMacroList().hashCode();
			}
			if (getRemoveMacroCount() > 0) {
				hash = (((hash * 37) + 8) * 53) + getRemoveMacroList().hashCode();
			}
			if (getAddMacroRuleNameCount() > 0) {
				hash = (((hash * 37) + 9) * 53) + getAddMacroRuleNameList().hashCode();
			}
			if (getRemoveMacroRuleNameCount() > 0) {
				hash = (((hash * 37) + 10) * 53) + getRemoveMacroRuleNameList().hashCode();
			}
			return (hash * 29) + this.unknownFields.hashCode();
		}

		static {
			defaultInstance.initFields();
			defaultInstance.makeImmutable();
		}

		/* access modifiers changed from: protected */
		public MessageLite internalImmutableDefault() {
			if (immutableDefault == null) {
				immutableDefault = internalImmutableDefault("com.google.analytics.containertag.proto.Serving$Rule");
			}
			return immutableDefault;
		}
	}

	public static final class CacheOption extends GeneratedMutableMessageLite<CacheOption> implements MutableMessageLite {
		public static final int EXPIRATION_SECONDS_FIELD_NUMBER = 2;
		public static final int GCACHE_EXPIRATION_SECONDS_FIELD_NUMBER = 3;
		public static final int LEVEL_FIELD_NUMBER = 1;
		private static final CacheOption defaultInstance = new CacheOption(true);
		public static Parser<CacheOption> PARSER = AbstractMutableMessageLite.internalNewParserForType(defaultInstance);
		private static volatile MessageLite immutableDefault = null;
		private static final long serialVersionUID = 0;
		private int bitField0_;
		private int expirationSeconds_;
		private int gcacheExpirationSeconds_;
		private CacheLevel level_ = CacheLevel.NO_CACHE;

		private CacheOption() {
			initFields();
		}

		private CacheOption(boolean noInit) {
		}

		public CacheOption newMessageForType() {
			return new CacheOption();
		}

		public static CacheOption newMessage() {
			return new CacheOption();
		}

		private void initFields() {
			this.level_ = CacheLevel.NO_CACHE;
		}

		public static CacheOption getDefaultInstance() {
			return defaultInstance;
		}

		public final CacheOption getDefaultInstanceForType() {
			return defaultInstance;
		}

		public Parser<CacheOption> getParserForType() {
			return PARSER;
		}

		public enum CacheLevel implements Internal.EnumLite {
			NO_CACHE(0, 1),
			PRIVATE(1, 2),
			PUBLIC(2, 3);

			public static final int NO_CACHE_VALUE = 1;
			public static final int PRIVATE_VALUE = 2;
			public static final int PUBLIC_VALUE = 3;
			private static Internal.EnumLiteMap<CacheLevel> internalValueMap;
			private final int value;

			static {
				internalValueMap = new Internal.EnumLiteMap<CacheLevel>() {
					public CacheLevel findValueByNumber(int number) {
						return CacheLevel.valueOf(number);
					}
				};
			}

			public final int getNumber() {
				return this.value;
			}

			public static CacheLevel valueOf(int value2) {
				switch (value2) {
					case 1:
						return NO_CACHE;
					case 2:
						return PRIVATE;
					case 3:
						return PUBLIC;
					default:
						return null;
				}
			}

			public static Internal.EnumLiteMap<CacheLevel> internalGetValueMap() {
				return internalValueMap;
			}

			private CacheLevel(int index, int value2) {
				this.value = value2;
			}
		}

		public boolean hasLevel() {
			return (this.bitField0_ & 1) == 1;
		}

		public CacheLevel getLevel() {
			return this.level_;
		}

		public CacheOption setLevel(CacheLevel value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 1;
			this.level_ = value;
			return this;
		}

		public CacheOption clearLevel() {
			assertMutable();
			this.bitField0_ &= -2;
			this.level_ = CacheLevel.NO_CACHE;
			return this;
		}

		public boolean hasExpirationSeconds() {
			return (this.bitField0_ & 2) == 2;
		}

		public int getExpirationSeconds() {
			return this.expirationSeconds_;
		}

		public CacheOption setExpirationSeconds(int value) {
			assertMutable();
			this.bitField0_ |= 2;
			this.expirationSeconds_ = value;
			return this;
		}

		public CacheOption clearExpirationSeconds() {
			assertMutable();
			this.bitField0_ &= -3;
			this.expirationSeconds_ = 0;
			return this;
		}

		public boolean hasGcacheExpirationSeconds() {
			return (this.bitField0_ & 4) == 4;
		}

		public int getGcacheExpirationSeconds() {
			return this.gcacheExpirationSeconds_;
		}

		public CacheOption setGcacheExpirationSeconds(int value) {
			assertMutable();
			this.bitField0_ |= 4;
			this.gcacheExpirationSeconds_ = value;
			return this;
		}

		public CacheOption clearGcacheExpirationSeconds() {
			assertMutable();
			this.bitField0_ &= -5;
			this.gcacheExpirationSeconds_ = 0;
			return this;
		}

		public final boolean isInitialized() {
			return true;
		}

		public CacheOption clone() {
			return newMessageForType().mergeFrom(this);
		}

		public CacheOption mergeFrom(CacheOption other) {
			if (this == other) {
				throw new IllegalArgumentException("mergeFrom(message) called on the same message.");
			}
			assertMutable();
			if (other != getDefaultInstance()) {
				if (other.hasLevel()) {
					setLevel(other.getLevel());
				}
				if (other.hasExpirationSeconds()) {
					setExpirationSeconds(other.getExpirationSeconds());
				}
				if (other.hasGcacheExpirationSeconds()) {
					setGcacheExpirationSeconds(other.getGcacheExpirationSeconds());
				}
				this.unknownFields = this.unknownFields.concat(other.unknownFields);
			}
			return this;
		}

		public boolean mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
			assertMutable();
			try {
				ByteString.Output unknownFieldsOutput = ByteString.newOutput();
				CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance((OutputStream) unknownFieldsOutput);
				boolean done = false;
				while (!done) {
					int tag = input.readTag();
					switch (tag) {
						case 0:
							done = true;
							break;
						case 8:
							int rawValue = input.readEnum();
							CacheLevel value = CacheLevel.valueOf(rawValue);
							if (value != null) {
								this.bitField0_ |= 1;
								this.level_ = value;
								break;
							} else {
								unknownFieldsCodedOutput.writeRawVarint32(tag);
								unknownFieldsCodedOutput.writeRawVarint32(rawValue);
								break;
							}
						case 16:
							this.bitField0_ |= 2;
							this.expirationSeconds_ = input.readInt32();
							break;
						case 24:
							this.bitField0_ |= 4;
							this.gcacheExpirationSeconds_ = input.readInt32();
							break;
						default:
							if (parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
								break;
							} else {
								done = true;
								break;
							}
					}
				}
				unknownFieldsCodedOutput.flush();
				this.unknownFields = unknownFieldsOutput.toByteString();
				return true;
			} catch (IOException e) {
				return false;
			}
		}

		public void writeToWithCachedSizes(CodedOutputStream output) throws IOException {
			int bytesWrittenBefore = output.getTotalBytesWritten();
			if ((this.bitField0_ & 1) == 1) {
				output.writeEnum(1, this.level_.getNumber());
			}
			if ((this.bitField0_ & 2) == 2) {
				output.writeInt32(2, this.expirationSeconds_);
			}
			if ((this.bitField0_ & 4) == 4) {
				output.writeInt32(3, this.gcacheExpirationSeconds_);
			}
			output.writeRawBytes(this.unknownFields);
			if (getCachedSize() != output.getTotalBytesWritten() - bytesWrittenBefore) {
				throw new RuntimeException("Serialized size doesn't match cached size. You may forget to call getSerializedSize() or the message is being modified concurrently.");
			}
		}

		public int getSerializedSize() {
			int size = 0;
			if ((this.bitField0_ & 1) == 1) {
				size = 0 + CodedOutputStream.computeEnumSize(1, this.level_.getNumber());
			}
			if ((this.bitField0_ & 2) == 2) {
				size += CodedOutputStream.computeInt32Size(2, this.expirationSeconds_);
			}
			if ((this.bitField0_ & 4) == 4) {
				size += CodedOutputStream.computeInt32Size(3, this.gcacheExpirationSeconds_);
			}
			int size2 = size + this.unknownFields.size();
			this.cachedSize = size2;
			return size2;
		}

		/* access modifiers changed from: protected */
		public Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public CacheOption clear() {
			assertMutable();
			super.clear();
			this.level_ = CacheLevel.NO_CACHE;
			this.bitField0_ &= -2;
			this.expirationSeconds_ = 0;
			this.bitField0_ &= -3;
			this.gcacheExpirationSeconds_ = 0;
			this.bitField0_ &= -5;
			return this;
		}

		public boolean equals(Object obj) {
			boolean result;
			boolean result2;
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof CacheOption)) {
				return super.equals(obj);
			}
			CacheOption other = (CacheOption) obj;
			boolean result3 = 1 != 0 && hasLevel() == other.hasLevel();
			if (hasLevel()) {
				result3 = result3 && getLevel() == other.getLevel();
			}
			if (!result3 || hasExpirationSeconds() != other.hasExpirationSeconds()) {
				result = false;
			} else {
				result = true;
			}
			if (hasExpirationSeconds()) {
				if (!result || getExpirationSeconds() != other.getExpirationSeconds()) {
					result = false;
				} else {
					result = true;
				}
			}
			if (!result || hasGcacheExpirationSeconds() != other.hasGcacheExpirationSeconds()) {
				result2 = false;
			} else {
				result2 = true;
			}
			if (hasGcacheExpirationSeconds()) {
				if (!result2 || getGcacheExpirationSeconds() != other.getGcacheExpirationSeconds()) {
					result2 = false;
				} else {
					result2 = true;
				}
			}
			return result2;
		}

		public int hashCode() {
			int hash = 41;
			if (hasLevel()) {
				int hash2 = 1517 + 1;
				hash = 80454 + Internal.hashEnum(getLevel());
			}
			if (hasExpirationSeconds()) {
				hash = (((hash * 37) + 2) * 53) + getExpirationSeconds();
			}
			if (hasGcacheExpirationSeconds()) {
				hash = (((hash * 37) + 3) * 53) + getGcacheExpirationSeconds();
			}
			return (hash * 29) + this.unknownFields.hashCode();
		}

		static {
			defaultInstance.initFields();
			defaultInstance.makeImmutable();
		}

		/* access modifiers changed from: protected */
		public MessageLite internalImmutableDefault() {
			if (immutableDefault == null) {
				immutableDefault = internalImmutableDefault("com.google.analytics.containertag.proto.Serving$CacheOption");
			}
			return immutableDefault;
		}
	}

	public static final class Resource extends GeneratedMutableMessageLite<Resource> implements MutableMessageLite {
		public static final int ENABLE_AUTO_EVENT_TRACKING_FIELD_NUMBER = 18;
		public static final int KEY_FIELD_NUMBER = 1;
		public static final int LIVE_JS_CACHE_OPTION_FIELD_NUMBER = 14;
		public static final int MACRO_FIELD_NUMBER = 4;
		public static final int MALWARE_SCAN_AUTH_CODE_FIELD_NUMBER = 10;
		public static final int PREDICATE_FIELD_NUMBER = 6;
		public static final int PREVIEW_AUTH_CODE_FIELD_NUMBER = 9;
		public static final int PROPERTY_FIELD_NUMBER = 3;
		public static final int REPORTING_SAMPLE_RATE_FIELD_NUMBER = 15;
		public static final int RESOURCE_FORMAT_VERSION_FIELD_NUMBER = 17;
		public static final int RULE_FIELD_NUMBER = 7;
		public static final int TAG_FIELD_NUMBER = 5;
		public static final int TEMPLATE_VERSION_SET_FIELD_NUMBER = 12;
		public static final int USAGE_CONTEXT_FIELD_NUMBER = 16;
		public static final int VALUE_FIELD_NUMBER = 2;
		public static final int VERSION_FIELD_NUMBER = 13;
		private static final Resource defaultInstance = new Resource(true);
		public static Parser<Resource> PARSER = AbstractMutableMessageLite.internalNewParserForType(defaultInstance);
		private static volatile MessageLite immutableDefault = null;
		private static final long serialVersionUID = 0;
		private int bitField0_;
		private boolean enableAutoEventTracking_;
		private LazyStringList key_ = null;
		private CacheOption liveJsCacheOption_;
		private List<FunctionCall> macro_ = null;
		private Object malwareScanAuthCode_ = Internal.EMPTY_BYTE_ARRAY;
		private List<FunctionCall> predicate_ = null;
		private Object previewAuthCode_ = Internal.EMPTY_BYTE_ARRAY;
		private List<Property> property_ = null;
		private float reportingSampleRate_;
		private int resourceFormatVersion_;
		private List<Rule> rule_ = null;
		private List<FunctionCall> tag_ = null;
		private Object templateVersionSet_ = Internal.byteArrayDefaultValue(GreeDefs.BARCODE);
		private LazyStringList usageContext_ = null;
		private List<MutableTypeSystem.Value> value_ = null;
		private Object version_ = Internal.EMPTY_BYTE_ARRAY;

		private Resource() {
			initFields();
		}

		private Resource(boolean noInit) {
		}

		public Resource newMessageForType() {
			return new Resource();
		}

		public static Resource newMessage() {
			return new Resource();
		}

		private void initFields() {
			this.liveJsCacheOption_ = CacheOption.getDefaultInstance();
		}

		public static Resource getDefaultInstance() {
			return defaultInstance;
		}

		public final Resource getDefaultInstanceForType() {
			return defaultInstance;
		}

		public Parser<Resource> getParserForType() {
			return PARSER;
		}

		private void ensureKeyInitialized() {
			if (this.key_ == null) {
				this.key_ = new LazyStringArrayList();
			}
		}

		public int getKeyCount() {
			if (this.key_ == null) {
				return 0;
			}
			return this.key_.size();
		}

		public List<String> getKeyList() {
			if (this.key_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.key_);
		}

		public List<byte[]> getKeyListAsBytes() {
			if (this.key_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.key_.asByteArrayList());
		}

		public List<String> getMutableKeyList() {
			assertMutable();
			ensureKeyInitialized();
			return this.key_;
		}

		public List<byte[]> getMutableKeyListAsBytes() {
			assertMutable();
			ensureKeyInitialized();
			return this.key_.asByteArrayList();
		}

		public String getKey(int index) {
			return (String) this.key_.get(index);
		}

		public byte[] getKeyAsBytes(int index) {
			return this.key_.getByteArray(index);
		}

		public Resource addKey(String value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureKeyInitialized();
			this.key_.add(value);
			return this;
		}

		public Resource addKeyAsBytes(byte[] value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureKeyInitialized();
			this.key_.add(value);
			return this;
		}

		public Resource addAllKey(Iterable<String> values) {
			assertMutable();
			ensureKeyInitialized();
			AbstractMutableMessageLite.addAll(values, this.key_);
			return this;
		}

		public Resource setKey(int index, String value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureKeyInitialized();
			this.key_.set(index, value);
			return this;
		}

		public Resource setKeyAsBytes(int index, byte[] value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureKeyInitialized();
			this.key_.set(index, value);
			return this;
		}

		public Resource clearKey() {
			assertMutable();
			this.key_ = null;
			return this;
		}

		private void ensureValueInitialized() {
			if (this.value_ == null) {
				this.value_ = new ArrayList();
			}
		}

		public int getValueCount() {
			if (this.value_ == null) {
				return 0;
			}
			return this.value_.size();
		}

		public List<MutableTypeSystem.Value> getValueList() {
			if (this.value_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.value_);
		}

		public List<MutableTypeSystem.Value> getMutableValueList() {
			assertMutable();
			ensureValueInitialized();
			return this.value_;
		}

		public MutableTypeSystem.Value getValue(int index) {
			return this.value_.get(index);
		}

		public MutableTypeSystem.Value getMutableValue(int index) {
			return this.value_.get(index);
		}

		public MutableTypeSystem.Value addValue() {
			assertMutable();
			ensureValueInitialized();
			MutableTypeSystem.Value value = MutableTypeSystem.Value.newMessage();
			this.value_.add(value);
			return value;
		}

		public Resource addValue(MutableTypeSystem.Value value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureValueInitialized();
			this.value_.add(value);
			return this;
		}

		public Resource addAllValue(Iterable<? extends MutableTypeSystem.Value> values) {
			assertMutable();
			ensureValueInitialized();
			AbstractMutableMessageLite.addAll(values, this.value_);
			return this;
		}

		public Resource setValue(int index, MutableTypeSystem.Value value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureValueInitialized();
			this.value_.set(index, value);
			return this;
		}

		public Resource clearValue() {
			assertMutable();
			this.value_ = null;
			return this;
		}

		private void ensurePropertyInitialized() {
			if (this.property_ == null) {
				this.property_ = new ArrayList();
			}
		}

		public int getPropertyCount() {
			if (this.property_ == null) {
				return 0;
			}
			return this.property_.size();
		}

		public List<Property> getPropertyList() {
			if (this.property_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.property_);
		}

		public List<Property> getMutablePropertyList() {
			assertMutable();
			ensurePropertyInitialized();
			return this.property_;
		}

		public Property getProperty(int index) {
			return this.property_.get(index);
		}

		public Property getMutableProperty(int index) {
			return this.property_.get(index);
		}

		public Property addProperty() {
			assertMutable();
			ensurePropertyInitialized();
			Property value = Property.newMessage();
			this.property_.add(value);
			return value;
		}

		public Resource addProperty(Property value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensurePropertyInitialized();
			this.property_.add(value);
			return this;
		}

		public Resource addAllProperty(Iterable<? extends Property> values) {
			assertMutable();
			ensurePropertyInitialized();
			AbstractMutableMessageLite.addAll(values, this.property_);
			return this;
		}

		public Resource setProperty(int index, Property value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensurePropertyInitialized();
			this.property_.set(index, value);
			return this;
		}

		public Resource clearProperty() {
			assertMutable();
			this.property_ = null;
			return this;
		}

		private void ensureMacroInitialized() {
			if (this.macro_ == null) {
				this.macro_ = new ArrayList();
			}
		}

		public int getMacroCount() {
			if (this.macro_ == null) {
				return 0;
			}
			return this.macro_.size();
		}

		public List<FunctionCall> getMacroList() {
			if (this.macro_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.macro_);
		}

		public List<FunctionCall> getMutableMacroList() {
			assertMutable();
			ensureMacroInitialized();
			return this.macro_;
		}

		public FunctionCall getMacro(int index) {
			return this.macro_.get(index);
		}

		public FunctionCall getMutableMacro(int index) {
			return this.macro_.get(index);
		}

		public FunctionCall addMacro() {
			assertMutable();
			ensureMacroInitialized();
			FunctionCall value = FunctionCall.newMessage();
			this.macro_.add(value);
			return value;
		}

		public Resource addMacro(FunctionCall value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureMacroInitialized();
			this.macro_.add(value);
			return this;
		}

		public Resource addAllMacro(Iterable<? extends FunctionCall> values) {
			assertMutable();
			ensureMacroInitialized();
			AbstractMutableMessageLite.addAll(values, this.macro_);
			return this;
		}

		public Resource setMacro(int index, FunctionCall value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureMacroInitialized();
			this.macro_.set(index, value);
			return this;
		}

		public Resource clearMacro() {
			assertMutable();
			this.macro_ = null;
			return this;
		}

		private void ensureTagInitialized() {
			if (this.tag_ == null) {
				this.tag_ = new ArrayList();
			}
		}

		public int getTagCount() {
			if (this.tag_ == null) {
				return 0;
			}
			return this.tag_.size();
		}

		public List<FunctionCall> getTagList() {
			if (this.tag_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.tag_);
		}

		public List<FunctionCall> getMutableTagList() {
			assertMutable();
			ensureTagInitialized();
			return this.tag_;
		}

		public FunctionCall getTag(int index) {
			return this.tag_.get(index);
		}

		public FunctionCall getMutableTag(int index) {
			return this.tag_.get(index);
		}

		public FunctionCall addTag() {
			assertMutable();
			ensureTagInitialized();
			FunctionCall value = FunctionCall.newMessage();
			this.tag_.add(value);
			return value;
		}

		public Resource addTag(FunctionCall value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureTagInitialized();
			this.tag_.add(value);
			return this;
		}

		public Resource addAllTag(Iterable<? extends FunctionCall> values) {
			assertMutable();
			ensureTagInitialized();
			AbstractMutableMessageLite.addAll(values, this.tag_);
			return this;
		}

		public Resource setTag(int index, FunctionCall value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureTagInitialized();
			this.tag_.set(index, value);
			return this;
		}

		public Resource clearTag() {
			assertMutable();
			this.tag_ = null;
			return this;
		}

		private void ensurePredicateInitialized() {
			if (this.predicate_ == null) {
				this.predicate_ = new ArrayList();
			}
		}

		public int getPredicateCount() {
			if (this.predicate_ == null) {
				return 0;
			}
			return this.predicate_.size();
		}

		public List<FunctionCall> getPredicateList() {
			if (this.predicate_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.predicate_);
		}

		public List<FunctionCall> getMutablePredicateList() {
			assertMutable();
			ensurePredicateInitialized();
			return this.predicate_;
		}

		public FunctionCall getPredicate(int index) {
			return this.predicate_.get(index);
		}

		public FunctionCall getMutablePredicate(int index) {
			return this.predicate_.get(index);
		}

		public FunctionCall addPredicate() {
			assertMutable();
			ensurePredicateInitialized();
			FunctionCall value = FunctionCall.newMessage();
			this.predicate_.add(value);
			return value;
		}

		public Resource addPredicate(FunctionCall value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensurePredicateInitialized();
			this.predicate_.add(value);
			return this;
		}

		public Resource addAllPredicate(Iterable<? extends FunctionCall> values) {
			assertMutable();
			ensurePredicateInitialized();
			AbstractMutableMessageLite.addAll(values, this.predicate_);
			return this;
		}

		public Resource setPredicate(int index, FunctionCall value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensurePredicateInitialized();
			this.predicate_.set(index, value);
			return this;
		}

		public Resource clearPredicate() {
			assertMutable();
			this.predicate_ = null;
			return this;
		}

		private void ensureRuleInitialized() {
			if (this.rule_ == null) {
				this.rule_ = new ArrayList();
			}
		}

		public int getRuleCount() {
			if (this.rule_ == null) {
				return 0;
			}
			return this.rule_.size();
		}

		public List<Rule> getRuleList() {
			if (this.rule_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.rule_);
		}

		public List<Rule> getMutableRuleList() {
			assertMutable();
			ensureRuleInitialized();
			return this.rule_;
		}

		public Rule getRule(int index) {
			return this.rule_.get(index);
		}

		public Rule getMutableRule(int index) {
			return this.rule_.get(index);
		}

		public Rule addRule() {
			assertMutable();
			ensureRuleInitialized();
			Rule value = Rule.newMessage();
			this.rule_.add(value);
			return value;
		}

		public Resource addRule(Rule value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureRuleInitialized();
			this.rule_.add(value);
			return this;
		}

		public Resource addAllRule(Iterable<? extends Rule> values) {
			assertMutable();
			ensureRuleInitialized();
			AbstractMutableMessageLite.addAll(values, this.rule_);
			return this;
		}

		public Resource setRule(int index, Rule value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureRuleInitialized();
			this.rule_.set(index, value);
			return this;
		}

		public Resource clearRule() {
			assertMutable();
			this.rule_ = null;
			return this;
		}

		public boolean hasPreviewAuthCode() {
			return (this.bitField0_ & 1) == 1;
		}

		public String getPreviewAuthCode() {
			Object ref = this.previewAuthCode_;
			if (ref instanceof String) {
				return (String) ref;
			}
			byte[] byteArray = (byte[]) ref;
			String s = Internal.toStringUtf8(byteArray);
			if (Internal.isValidUtf8(byteArray)) {
				this.previewAuthCode_ = s;
			}
			return s;
		}

		public byte[] getPreviewAuthCodeAsBytes() {
			Object ref = this.previewAuthCode_;
			if (!(ref instanceof String)) {
				return (byte[]) ref;
			}
			byte[] byteArray = Internal.toByteArray((String) ref);
			this.previewAuthCode_ = byteArray;
			return byteArray;
		}

		public Resource setPreviewAuthCode(String value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 1;
			this.previewAuthCode_ = value;
			return this;
		}

		public Resource setPreviewAuthCodeAsBytes(byte[] value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 1;
			this.previewAuthCode_ = value;
			return this;
		}

		public Resource clearPreviewAuthCode() {
			assertMutable();
			this.bitField0_ &= -2;
			this.previewAuthCode_ = Internal.EMPTY_BYTE_ARRAY;
			return this;
		}

		public boolean hasMalwareScanAuthCode() {
			return (this.bitField0_ & 2) == 2;
		}

		public String getMalwareScanAuthCode() {
			Object ref = this.malwareScanAuthCode_;
			if (ref instanceof String) {
				return (String) ref;
			}
			byte[] byteArray = (byte[]) ref;
			String s = Internal.toStringUtf8(byteArray);
			if (Internal.isValidUtf8(byteArray)) {
				this.malwareScanAuthCode_ = s;
			}
			return s;
		}

		public byte[] getMalwareScanAuthCodeAsBytes() {
			Object ref = this.malwareScanAuthCode_;
			if (!(ref instanceof String)) {
				return (byte[]) ref;
			}
			byte[] byteArray = Internal.toByteArray((String) ref);
			this.malwareScanAuthCode_ = byteArray;
			return byteArray;
		}

		public Resource setMalwareScanAuthCode(String value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 2;
			this.malwareScanAuthCode_ = value;
			return this;
		}

		public Resource setMalwareScanAuthCodeAsBytes(byte[] value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 2;
			this.malwareScanAuthCode_ = value;
			return this;
		}

		public Resource clearMalwareScanAuthCode() {
			assertMutable();
			this.bitField0_ &= -3;
			this.malwareScanAuthCode_ = Internal.EMPTY_BYTE_ARRAY;
			return this;
		}

		public boolean hasTemplateVersionSet() {
			return (this.bitField0_ & 4) == 4;
		}

		public String getTemplateVersionSet() {
			Object ref = this.templateVersionSet_;
			if (ref instanceof String) {
				return (String) ref;
			}
			byte[] byteArray = (byte[]) ref;
			String s = Internal.toStringUtf8(byteArray);
			if (Internal.isValidUtf8(byteArray)) {
				this.templateVersionSet_ = s;
			}
			return s;
		}

		public byte[] getTemplateVersionSetAsBytes() {
			Object ref = this.templateVersionSet_;
			if (!(ref instanceof String)) {
				return (byte[]) ref;
			}
			byte[] byteArray = Internal.toByteArray((String) ref);
			this.templateVersionSet_ = byteArray;
			return byteArray;
		}

		public Resource setTemplateVersionSet(String value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 4;
			this.templateVersionSet_ = value;
			return this;
		}

		public Resource setTemplateVersionSetAsBytes(byte[] value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 4;
			this.templateVersionSet_ = value;
			return this;
		}

		public Resource clearTemplateVersionSet() {
			assertMutable();
			this.bitField0_ &= -5;
			this.templateVersionSet_ = getDefaultInstance().getTemplateVersionSetAsBytes();
			return this;
		}

		public boolean hasVersion() {
			return (this.bitField0_ & 8) == 8;
		}

		public String getVersion() {
			Object ref = this.version_;
			if (ref instanceof String) {
				return (String) ref;
			}
			byte[] byteArray = (byte[]) ref;
			String s = Internal.toStringUtf8(byteArray);
			if (Internal.isValidUtf8(byteArray)) {
				this.version_ = s;
			}
			return s;
		}

		public byte[] getVersionAsBytes() {
			Object ref = this.version_;
			if (!(ref instanceof String)) {
				return (byte[]) ref;
			}
			byte[] byteArray = Internal.toByteArray((String) ref);
			this.version_ = byteArray;
			return byteArray;
		}

		public Resource setVersion(String value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 8;
			this.version_ = value;
			return this;
		}

		public Resource setVersionAsBytes(byte[] value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 8;
			this.version_ = value;
			return this;
		}

		public Resource clearVersion() {
			assertMutable();
			this.bitField0_ &= -9;
			this.version_ = Internal.EMPTY_BYTE_ARRAY;
			return this;
		}

		private void ensureLiveJsCacheOptionInitialized() {
			if (this.liveJsCacheOption_ == CacheOption.getDefaultInstance()) {
				this.liveJsCacheOption_ = CacheOption.newMessage();
			}
		}

		public boolean hasLiveJsCacheOption() {
			return (this.bitField0_ & 16) == 16;
		}

		public CacheOption getLiveJsCacheOption() {
			return this.liveJsCacheOption_;
		}

		public CacheOption getMutableLiveJsCacheOption() {
			assertMutable();
			ensureLiveJsCacheOptionInitialized();
			this.bitField0_ |= 16;
			return this.liveJsCacheOption_;
		}

		public Resource setLiveJsCacheOption(CacheOption value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 16;
			this.liveJsCacheOption_ = value;
			return this;
		}

		public Resource clearLiveJsCacheOption() {
			assertMutable();
			this.bitField0_ &= -17;
			if (this.liveJsCacheOption_ != CacheOption.getDefaultInstance()) {
				this.liveJsCacheOption_.clear();
			}
			return this;
		}

		public boolean hasReportingSampleRate() {
			return (this.bitField0_ & 32) == 32;
		}

		public float getReportingSampleRate() {
			return this.reportingSampleRate_;
		}

		public Resource setReportingSampleRate(float value) {
			assertMutable();
			this.bitField0_ |= 32;
			this.reportingSampleRate_ = value;
			return this;
		}

		public Resource clearReportingSampleRate() {
			assertMutable();
			this.bitField0_ &= -33;
			this.reportingSampleRate_ = 0.0f;
			return this;
		}

		public boolean hasEnableAutoEventTracking() {
			return (this.bitField0_ & 64) == 64;
		}

		public boolean getEnableAutoEventTracking() {
			return this.enableAutoEventTracking_;
		}

		public Resource setEnableAutoEventTracking(boolean value) {
			assertMutable();
			this.bitField0_ |= 64;
			this.enableAutoEventTracking_ = value;
			return this;
		}

		public Resource clearEnableAutoEventTracking() {
			assertMutable();
			this.bitField0_ &= -65;
			this.enableAutoEventTracking_ = false;
			return this;
		}

		private void ensureUsageContextInitialized() {
			if (this.usageContext_ == null) {
				this.usageContext_ = new LazyStringArrayList();
			}
		}

		public int getUsageContextCount() {
			if (this.usageContext_ == null) {
				return 0;
			}
			return this.usageContext_.size();
		}

		public List<String> getUsageContextList() {
			if (this.usageContext_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.usageContext_);
		}

		public List<byte[]> getUsageContextListAsBytes() {
			if (this.usageContext_ == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(this.usageContext_.asByteArrayList());
		}

		public List<String> getMutableUsageContextList() {
			assertMutable();
			ensureUsageContextInitialized();
			return this.usageContext_;
		}

		public List<byte[]> getMutableUsageContextListAsBytes() {
			assertMutable();
			ensureUsageContextInitialized();
			return this.usageContext_.asByteArrayList();
		}

		public String getUsageContext(int index) {
			return (String) this.usageContext_.get(index);
		}

		public byte[] getUsageContextAsBytes(int index) {
			return this.usageContext_.getByteArray(index);
		}

		public Resource addUsageContext(String value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureUsageContextInitialized();
			this.usageContext_.add(value);
			return this;
		}

		public Resource addUsageContextAsBytes(byte[] value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureUsageContextInitialized();
			this.usageContext_.add(value);
			return this;
		}

		public Resource addAllUsageContext(Iterable<String> values) {
			assertMutable();
			ensureUsageContextInitialized();
			AbstractMutableMessageLite.addAll(values, this.usageContext_);
			return this;
		}

		public Resource setUsageContext(int index, String value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureUsageContextInitialized();
			this.usageContext_.set(index, value);
			return this;
		}

		public Resource setUsageContextAsBytes(int index, byte[] value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			ensureUsageContextInitialized();
			this.usageContext_.set(index, value);
			return this;
		}

		public Resource clearUsageContext() {
			assertMutable();
			this.usageContext_ = null;
			return this;
		}

		public boolean hasResourceFormatVersion() {
			return (this.bitField0_ & 128) == 128;
		}

		public int getResourceFormatVersion() {
			return this.resourceFormatVersion_;
		}

		public Resource setResourceFormatVersion(int value) {
			assertMutable();
			this.bitField0_ |= 128;
			this.resourceFormatVersion_ = value;
			return this;
		}

		public Resource clearResourceFormatVersion() {
			assertMutable();
			this.bitField0_ &= -129;
			this.resourceFormatVersion_ = 0;
			return this;
		}

		public final boolean isInitialized() {
			for (int i = 0; i < getValueCount(); i++) {
				if (!getValue(i).isInitialized()) {
					return false;
				}
			}
			for (int i2 = 0; i2 < getPropertyCount(); i2++) {
				if (!getProperty(i2).isInitialized()) {
					return false;
				}
			}
			for (int i3 = 0; i3 < getMacroCount(); i3++) {
				if (!getMacro(i3).isInitialized()) {
					return false;
				}
			}
			for (int i4 = 0; i4 < getTagCount(); i4++) {
				if (!getTag(i4).isInitialized()) {
					return false;
				}
			}
			for (int i5 = 0; i5 < getPredicateCount(); i5++) {
				if (!getPredicate(i5).isInitialized()) {
					return false;
				}
			}
			return true;
		}

		public Resource clone() {
			return newMessageForType().mergeFrom(this);
		}

		public Resource mergeFrom(Resource other) {
			if (this == other) {
				throw new IllegalArgumentException("mergeFrom(message) called on the same message.");
			}
			assertMutable();
			if (other != getDefaultInstance()) {
				if (other.key_ != null && !other.key_.isEmpty()) {
					ensureKeyInitialized();
					this.key_.mergeFrom(other.key_);
				}
				if (other.value_ != null && !other.value_.isEmpty()) {
					ensureValueInitialized();
					AbstractMutableMessageLite.addAll(other.value_, this.value_);
				}
				if (other.property_ != null && !other.property_.isEmpty()) {
					ensurePropertyInitialized();
					AbstractMutableMessageLite.addAll(other.property_, this.property_);
				}
				if (other.macro_ != null && !other.macro_.isEmpty()) {
					ensureMacroInitialized();
					AbstractMutableMessageLite.addAll(other.macro_, this.macro_);
				}
				if (other.tag_ != null && !other.tag_.isEmpty()) {
					ensureTagInitialized();
					AbstractMutableMessageLite.addAll(other.tag_, this.tag_);
				}
				if (other.predicate_ != null && !other.predicate_.isEmpty()) {
					ensurePredicateInitialized();
					AbstractMutableMessageLite.addAll(other.predicate_, this.predicate_);
				}
				if (other.rule_ != null && !other.rule_.isEmpty()) {
					ensureRuleInitialized();
					AbstractMutableMessageLite.addAll(other.rule_, this.rule_);
				}
				if (other.hasPreviewAuthCode()) {
					this.bitField0_ |= 1;
					if (other.previewAuthCode_ instanceof String) {
						this.previewAuthCode_ = other.previewAuthCode_;
					} else {
						byte[] ba = (byte[]) other.previewAuthCode_;
						this.previewAuthCode_ = Arrays.copyOf(ba, ba.length);
					}
				}
				if (other.hasMalwareScanAuthCode()) {
					this.bitField0_ |= 2;
					if (other.malwareScanAuthCode_ instanceof String) {
						this.malwareScanAuthCode_ = other.malwareScanAuthCode_;
					} else {
						byte[] ba2 = (byte[]) other.malwareScanAuthCode_;
						this.malwareScanAuthCode_ = Arrays.copyOf(ba2, ba2.length);
					}
				}
				if (other.hasTemplateVersionSet()) {
					this.bitField0_ |= 4;
					if (other.templateVersionSet_ instanceof String) {
						this.templateVersionSet_ = other.templateVersionSet_;
					} else {
						byte[] ba3 = (byte[]) other.templateVersionSet_;
						this.templateVersionSet_ = Arrays.copyOf(ba3, ba3.length);
					}
				}
				if (other.hasVersion()) {
					this.bitField0_ |= 8;
					if (other.version_ instanceof String) {
						this.version_ = other.version_;
					} else {
						byte[] ba4 = (byte[]) other.version_;
						this.version_ = Arrays.copyOf(ba4, ba4.length);
					}
				}
				if (other.hasLiveJsCacheOption()) {
					ensureLiveJsCacheOptionInitialized();
					this.liveJsCacheOption_.mergeFrom(other.getLiveJsCacheOption());
					this.bitField0_ |= 16;
				}
				if (other.hasReportingSampleRate()) {
					setReportingSampleRate(other.getReportingSampleRate());
				}
				if (other.usageContext_ != null && !other.usageContext_.isEmpty()) {
					ensureUsageContextInitialized();
					this.usageContext_.mergeFrom(other.usageContext_);
				}
				if (other.hasResourceFormatVersion()) {
					setResourceFormatVersion(other.getResourceFormatVersion());
				}
				if (other.hasEnableAutoEventTracking()) {
					setEnableAutoEventTracking(other.getEnableAutoEventTracking());
				}
				this.unknownFields = this.unknownFields.concat(other.unknownFields);
			}
			return this;
		}

		public boolean mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
			assertMutable();
			try {
				ByteString.Output unknownFieldsOutput = ByteString.newOutput();
				CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance((OutputStream) unknownFieldsOutput);
				boolean done = false;
				while (!done) {
					int tag = input.readTag();
					switch (tag) {
						case 0:
							done = true;
							break;
						case 10:
							ensureKeyInitialized();
							this.key_.add(input.readByteArray());
							break;
						case 18:
							input.readMessage((MutableMessageLite) addValue(), extensionRegistry);
							break;
						case 26:
							input.readMessage((MutableMessageLite) addProperty(), extensionRegistry);
							break;
						case 34:
							input.readMessage((MutableMessageLite) addMacro(), extensionRegistry);
							break;
						case 42:
							input.readMessage((MutableMessageLite) addTag(), extensionRegistry);
							break;
						case 50:
							input.readMessage((MutableMessageLite) addPredicate(), extensionRegistry);
							break;
						case 58:
							input.readMessage((MutableMessageLite) addRule(), extensionRegistry);
							break;
						case 74:
							this.bitField0_ |= 1;
							this.previewAuthCode_ = input.readByteArray();
							break;
						case 82:
							this.bitField0_ |= 2;
							this.malwareScanAuthCode_ = input.readByteArray();
							break;
						case 98:
							this.bitField0_ |= 4;
							this.templateVersionSet_ = input.readByteArray();
							break;
						case BaseInterface.RESULT_LOG_IN:
							this.bitField0_ |= 8;
							this.version_ = input.readByteArray();
							break;
						case 114:
							if (this.liveJsCacheOption_ == CacheOption.getDefaultInstance()) {
								this.liveJsCacheOption_ = CacheOption.newMessage();
							}
							this.bitField0_ |= 16;
							input.readMessage((MutableMessageLite) this.liveJsCacheOption_, extensionRegistry);
							break;
						case 125:
							this.bitField0_ |= 32;
							this.reportingSampleRate_ = input.readFloat();
							break;
						case 130:
							ensureUsageContextInitialized();
							this.usageContext_.add(input.readByteArray());
							break;
						case 136:
							this.bitField0_ |= 128;
							this.resourceFormatVersion_ = input.readInt32();
							break;
						case 144:
							this.bitField0_ |= 64;
							this.enableAutoEventTracking_ = input.readBool();
							break;
						default:
							if (parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
								break;
							} else {
								done = true;
								break;
							}
					}
				}
				unknownFieldsCodedOutput.flush();
				this.unknownFields = unknownFieldsOutput.toByteString();
				return true;
			} catch (IOException e) {
				return false;
			}
		}

		public void writeToWithCachedSizes(CodedOutputStream output) throws IOException {
			int bytesWrittenBefore = output.getTotalBytesWritten();
			if (this.key_ != null) {
				for (int i = 0; i < this.key_.size(); i++) {
					output.writeByteArray(1, this.key_.getByteArray(i));
				}
			}
			if (this.value_ != null) {
				for (int i2 = 0; i2 < this.value_.size(); i2++) {
					output.writeMessageWithCachedSizes(2, this.value_.get(i2));
				}
			}
			if (this.property_ != null) {
				for (int i3 = 0; i3 < this.property_.size(); i3++) {
					output.writeMessageWithCachedSizes(3, this.property_.get(i3));
				}
			}
			if (this.macro_ != null) {
				for (int i4 = 0; i4 < this.macro_.size(); i4++) {
					output.writeMessageWithCachedSizes(4, this.macro_.get(i4));
				}
			}
			if (this.tag_ != null) {
				for (int i5 = 0; i5 < this.tag_.size(); i5++) {
					output.writeMessageWithCachedSizes(5, this.tag_.get(i5));
				}
			}
			if (this.predicate_ != null) {
				for (int i6 = 0; i6 < this.predicate_.size(); i6++) {
					output.writeMessageWithCachedSizes(6, this.predicate_.get(i6));
				}
			}
			if (this.rule_ != null) {
				for (int i7 = 0; i7 < this.rule_.size(); i7++) {
					output.writeMessageWithCachedSizes(7, this.rule_.get(i7));
				}
			}
			if ((this.bitField0_ & 1) == 1) {
				output.writeByteArray(9, getPreviewAuthCodeAsBytes());
			}
			if ((this.bitField0_ & 2) == 2) {
				output.writeByteArray(10, getMalwareScanAuthCodeAsBytes());
			}
			if ((this.bitField0_ & 4) == 4) {
				output.writeByteArray(12, getTemplateVersionSetAsBytes());
			}
			if ((this.bitField0_ & 8) == 8) {
				output.writeByteArray(13, getVersionAsBytes());
			}
			if ((this.bitField0_ & 16) == 16) {
				output.writeMessageWithCachedSizes(14, this.liveJsCacheOption_);
			}
			if ((this.bitField0_ & 32) == 32) {
				output.writeFloat(15, this.reportingSampleRate_);
			}
			if (this.usageContext_ != null) {
				for (int i8 = 0; i8 < this.usageContext_.size(); i8++) {
					output.writeByteArray(16, this.usageContext_.getByteArray(i8));
				}
			}
			if ((this.bitField0_ & 128) == 128) {
				output.writeInt32(17, this.resourceFormatVersion_);
			}
			if ((this.bitField0_ & 64) == 64) {
				output.writeBool(18, this.enableAutoEventTracking_);
			}
			output.writeRawBytes(this.unknownFields);
			if (getCachedSize() != output.getTotalBytesWritten() - bytesWrittenBefore) {
				throw new RuntimeException("Serialized size doesn't match cached size. You may forget to call getSerializedSize() or the message is being modified concurrently.");
			}
		}

		public int getSerializedSize() {
			int size = 0;
			if (this.key_ != null && this.key_.size() > 0) {
				int dataSize = 0;
				for (int i = 0; i < this.key_.size(); i++) {
					dataSize += CodedOutputStream.computeByteArraySizeNoTag(this.key_.getByteArray(i));
				}
				size = 0 + dataSize + (this.key_.size() * 1);
			}
			if (this.value_ != null) {
				for (int i2 = 0; i2 < this.value_.size(); i2++) {
					size += CodedOutputStream.computeMessageSize(2, this.value_.get(i2));
				}
			}
			if (this.property_ != null) {
				for (int i3 = 0; i3 < this.property_.size(); i3++) {
					size += CodedOutputStream.computeMessageSize(3, this.property_.get(i3));
				}
			}
			if (this.macro_ != null) {
				for (int i4 = 0; i4 < this.macro_.size(); i4++) {
					size += CodedOutputStream.computeMessageSize(4, this.macro_.get(i4));
				}
			}
			if (this.tag_ != null) {
				for (int i5 = 0; i5 < this.tag_.size(); i5++) {
					size += CodedOutputStream.computeMessageSize(5, this.tag_.get(i5));
				}
			}
			if (this.predicate_ != null) {
				for (int i6 = 0; i6 < this.predicate_.size(); i6++) {
					size += CodedOutputStream.computeMessageSize(6, this.predicate_.get(i6));
				}
			}
			if (this.rule_ != null) {
				for (int i7 = 0; i7 < this.rule_.size(); i7++) {
					size += CodedOutputStream.computeMessageSize(7, this.rule_.get(i7));
				}
			}
			if ((this.bitField0_ & 1) == 1) {
				size += CodedOutputStream.computeByteArraySize(9, getPreviewAuthCodeAsBytes());
			}
			if ((this.bitField0_ & 2) == 2) {
				size += CodedOutputStream.computeByteArraySize(10, getMalwareScanAuthCodeAsBytes());
			}
			if ((this.bitField0_ & 4) == 4) {
				size += CodedOutputStream.computeByteArraySize(12, getTemplateVersionSetAsBytes());
			}
			if ((this.bitField0_ & 8) == 8) {
				size += CodedOutputStream.computeByteArraySize(13, getVersionAsBytes());
			}
			if ((this.bitField0_ & 16) == 16) {
				size += CodedOutputStream.computeMessageSize(14, this.liveJsCacheOption_);
			}
			if ((this.bitField0_ & 32) == 32) {
				size += CodedOutputStream.computeFloatSize(15, this.reportingSampleRate_);
			}
			if ((this.bitField0_ & 64) == 64) {
				size += CodedOutputStream.computeBoolSize(18, this.enableAutoEventTracking_);
			}
			if (this.usageContext_ != null && this.usageContext_.size() > 0) {
				int dataSize2 = 0;
				for (int i8 = 0; i8 < this.usageContext_.size(); i8++) {
					dataSize2 += CodedOutputStream.computeByteArraySizeNoTag(this.usageContext_.getByteArray(i8));
				}
				size = size + dataSize2 + (this.usageContext_.size() * 2);
			}
			if ((this.bitField0_ & 128) == 128) {
				size += CodedOutputStream.computeInt32Size(17, this.resourceFormatVersion_);
			}
			int size2 = size + this.unknownFields.size();
			this.cachedSize = size2;
			return size2;
		}

		/* access modifiers changed from: protected */
		public Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public Resource clear() {
			assertMutable();
			super.clear();
			this.key_ = null;
			this.value_ = null;
			this.property_ = null;
			this.macro_ = null;
			this.tag_ = null;
			this.predicate_ = null;
			this.rule_ = null;
			this.previewAuthCode_ = Internal.EMPTY_BYTE_ARRAY;
			this.bitField0_ &= -2;
			this.malwareScanAuthCode_ = Internal.EMPTY_BYTE_ARRAY;
			this.bitField0_ &= -3;
			this.templateVersionSet_ = getDefaultInstance().getTemplateVersionSetAsBytes();
			this.bitField0_ &= -5;
			this.version_ = Internal.EMPTY_BYTE_ARRAY;
			this.bitField0_ &= -9;
			if (this.liveJsCacheOption_ != CacheOption.getDefaultInstance()) {
				this.liveJsCacheOption_.clear();
			}
			this.bitField0_ &= -17;
			this.reportingSampleRate_ = 0.0f;
			this.bitField0_ &= -33;
			this.enableAutoEventTracking_ = false;
			this.bitField0_ &= -65;
			this.usageContext_ = null;
			this.resourceFormatVersion_ = 0;
			this.bitField0_ &= -129;
			return this;
		}

		public boolean equals(Object obj) {
			boolean result;
			boolean result2;
			boolean result3;
			boolean result4;
			boolean result5;
			boolean result6;
			boolean result7;
			boolean result8;
			boolean result9;
			boolean result10;
			boolean result11;
			boolean result12;
			boolean result13;
			boolean result14;
			boolean result15;
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof Resource)) {
				return super.equals(obj);
			}
			Resource other = (Resource) obj;
			if (!(1 != 0 && getKeyList().equals(other.getKeyList())) || !getValueList().equals(other.getValueList())) {
				result = false;
			} else {
				result = true;
			}
			if (!result || !getPropertyList().equals(other.getPropertyList())) {
				result2 = false;
			} else {
				result2 = true;
			}
			if (!result2 || !getMacroList().equals(other.getMacroList())) {
				result3 = false;
			} else {
				result3 = true;
			}
			if (!result3 || !getTagList().equals(other.getTagList())) {
				result4 = false;
			} else {
				result4 = true;
			}
			if (!result4 || !getPredicateList().equals(other.getPredicateList())) {
				result5 = false;
			} else {
				result5 = true;
			}
			if (!result5 || !getRuleList().equals(other.getRuleList())) {
				result6 = false;
			} else {
				result6 = true;
			}
			if (!result6 || hasPreviewAuthCode() != other.hasPreviewAuthCode()) {
				result7 = false;
			} else {
				result7 = true;
			}
			if (hasPreviewAuthCode()) {
				if (!result7 || !getPreviewAuthCode().equals(other.getPreviewAuthCode())) {
					result7 = false;
				} else {
					result7 = true;
				}
			}
			if (!result7 || hasMalwareScanAuthCode() != other.hasMalwareScanAuthCode()) {
				result8 = false;
			} else {
				result8 = true;
			}
			if (hasMalwareScanAuthCode()) {
				if (!result8 || !getMalwareScanAuthCode().equals(other.getMalwareScanAuthCode())) {
					result8 = false;
				} else {
					result8 = true;
				}
			}
			if (!result8 || hasTemplateVersionSet() != other.hasTemplateVersionSet()) {
				result9 = false;
			} else {
				result9 = true;
			}
			if (hasTemplateVersionSet()) {
				if (!result9 || !getTemplateVersionSet().equals(other.getTemplateVersionSet())) {
					result9 = false;
				} else {
					result9 = true;
				}
			}
			if (!result9 || hasVersion() != other.hasVersion()) {
				result10 = false;
			} else {
				result10 = true;
			}
			if (hasVersion()) {
				if (!result10 || !getVersion().equals(other.getVersion())) {
					result10 = false;
				} else {
					result10 = true;
				}
			}
			if (!result10 || hasLiveJsCacheOption() != other.hasLiveJsCacheOption()) {
				result11 = false;
			} else {
				result11 = true;
			}
			if (hasLiveJsCacheOption()) {
				if (!result11 || !getLiveJsCacheOption().equals(other.getLiveJsCacheOption())) {
					result11 = false;
				} else {
					result11 = true;
				}
			}
			if (!result11 || hasReportingSampleRate() != other.hasReportingSampleRate()) {
				result12 = false;
			} else {
				result12 = true;
			}
			if (hasReportingSampleRate()) {
				if (!result12 || Float.floatToIntBits(getReportingSampleRate()) != Float.floatToIntBits(other.getReportingSampleRate())) {
					result12 = false;
				} else {
					result12 = true;
				}
			}
			if (!result12 || hasEnableAutoEventTracking() != other.hasEnableAutoEventTracking()) {
				result13 = false;
			} else {
				result13 = true;
			}
			if (hasEnableAutoEventTracking()) {
				if (!result13 || getEnableAutoEventTracking() != other.getEnableAutoEventTracking()) {
					result13 = false;
				} else {
					result13 = true;
				}
			}
			if (!result13 || !getUsageContextList().equals(other.getUsageContextList())) {
				result14 = false;
			} else {
				result14 = true;
			}
			if (!result14 || hasResourceFormatVersion() != other.hasResourceFormatVersion()) {
				result15 = false;
			} else {
				result15 = true;
			}
			if (hasResourceFormatVersion()) {
				if (!result15 || getResourceFormatVersion() != other.getResourceFormatVersion()) {
					result15 = false;
				} else {
					result15 = true;
				}
			}
			return result15;
		}

		public int hashCode() {
			int hash = 41;
			if (getKeyCount() > 0) {
				int hash2 = 1517 + 1;
				hash = 80454 + getKeyList().hashCode();
			}
			if (getValueCount() > 0) {
				hash = (((hash * 37) + 2) * 53) + getValueList().hashCode();
			}
			if (getPropertyCount() > 0) {
				hash = (((hash * 37) + 3) * 53) + getPropertyList().hashCode();
			}
			if (getMacroCount() > 0) {
				hash = (((hash * 37) + 4) * 53) + getMacroList().hashCode();
			}
			if (getTagCount() > 0) {
				hash = (((hash * 37) + 5) * 53) + getTagList().hashCode();
			}
			if (getPredicateCount() > 0) {
				hash = (((hash * 37) + 6) * 53) + getPredicateList().hashCode();
			}
			if (getRuleCount() > 0) {
				hash = (((hash * 37) + 7) * 53) + getRuleList().hashCode();
			}
			if (hasPreviewAuthCode()) {
				hash = (((hash * 37) + 9) * 53) + getPreviewAuthCode().hashCode();
			}
			if (hasMalwareScanAuthCode()) {
				hash = (((hash * 37) + 10) * 53) + getMalwareScanAuthCode().hashCode();
			}
			if (hasTemplateVersionSet()) {
				hash = (((hash * 37) + 12) * 53) + getTemplateVersionSet().hashCode();
			}
			if (hasVersion()) {
				hash = (((hash * 37) + 13) * 53) + getVersion().hashCode();
			}
			if (hasLiveJsCacheOption()) {
				hash = (((hash * 37) + 14) * 53) + getLiveJsCacheOption().hashCode();
			}
			if (hasReportingSampleRate()) {
				hash = (((hash * 37) + 15) * 53) + Float.floatToIntBits(getReportingSampleRate());
			}
			if (hasEnableAutoEventTracking()) {
				hash = (((hash * 37) + 18) * 53) + Internal.hashBoolean(getEnableAutoEventTracking());
			}
			if (getUsageContextCount() > 0) {
				hash = (((hash * 37) + 16) * 53) + getUsageContextList().hashCode();
			}
			if (hasResourceFormatVersion()) {
				hash = (((hash * 37) + 17) * 53) + getResourceFormatVersion();
			}
			return (hash * 29) + this.unknownFields.hashCode();
		}

		static {
			defaultInstance.initFields();
			defaultInstance.makeImmutable();
		}

		/* access modifiers changed from: protected */
		public MessageLite internalImmutableDefault() {
			if (immutableDefault == null) {
				immutableDefault = internalImmutableDefault("com.google.analytics.containertag.proto.Serving$Resource");
			}
			return immutableDefault;
		}
	}

	public static final class OptionalResource extends GeneratedMutableMessageLite<OptionalResource> implements MutableMessageLite {
		public static final int RESOURCE_FIELD_NUMBER = 2;
		private static final OptionalResource defaultInstance = new OptionalResource(true);
		public static Parser<OptionalResource> PARSER = AbstractMutableMessageLite.internalNewParserForType(defaultInstance);
		private static volatile MessageLite immutableDefault = null;
		private static final long serialVersionUID = 0;
		private int bitField0_;
		private Resource resource_;

		private OptionalResource() {
			initFields();
		}

		private OptionalResource(boolean noInit) {
		}

		public OptionalResource newMessageForType() {
			return new OptionalResource();
		}

		public static OptionalResource newMessage() {
			return new OptionalResource();
		}

		private void initFields() {
			this.resource_ = Resource.getDefaultInstance();
		}

		public static OptionalResource getDefaultInstance() {
			return defaultInstance;
		}

		public final OptionalResource getDefaultInstanceForType() {
			return defaultInstance;
		}

		public Parser<OptionalResource> getParserForType() {
			return PARSER;
		}

		private void ensureResourceInitialized() {
			if (this.resource_ == Resource.getDefaultInstance()) {
				this.resource_ = Resource.newMessage();
			}
		}

		public boolean hasResource() {
			return (this.bitField0_ & 1) == 1;
		}

		public Resource getResource() {
			return this.resource_;
		}

		public Resource getMutableResource() {
			assertMutable();
			ensureResourceInitialized();
			this.bitField0_ |= 1;
			return this.resource_;
		}

		public OptionalResource setResource(Resource value) {
			assertMutable();
			if (value == null) {
				throw new NullPointerException();
			}
			this.bitField0_ |= 1;
			this.resource_ = value;
			return this;
		}

		public OptionalResource clearResource() {
			assertMutable();
			this.bitField0_ &= -2;
			if (this.resource_ != Resource.getDefaultInstance()) {
				this.resource_.clear();
			}
			return this;
		}

		public final boolean isInitialized() {
			if (!hasResource() || getResource().isInitialized()) {
				return true;
			}
			return false;
		}

		public OptionalResource clone() {
			return newMessageForType().mergeFrom(this);
		}

		public OptionalResource mergeFrom(OptionalResource other) {
			if (this == other) {
				throw new IllegalArgumentException("mergeFrom(message) called on the same message.");
			}
			assertMutable();
			if (other != getDefaultInstance()) {
				if (other.hasResource()) {
					ensureResourceInitialized();
					this.resource_.mergeFrom(other.getResource());
					this.bitField0_ |= 1;
				}
				this.unknownFields = this.unknownFields.concat(other.unknownFields);
			}
			return this;
		}

		public boolean mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
			assertMutable();
			try {
				ByteString.Output unknownFieldsOutput = ByteString.newOutput();
				CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance((OutputStream) unknownFieldsOutput);
				boolean done = false;
				while (!done) {
					int tag = input.readTag();
					switch (tag) {
						case 0:
							done = true;
							break;
						case 18:
							if (this.resource_ == Resource.getDefaultInstance()) {
								this.resource_ = Resource.newMessage();
							}
							this.bitField0_ |= 1;
							input.readMessage((MutableMessageLite) this.resource_, extensionRegistry);
							break;
						default:
							if (parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
								break;
							} else {
								done = true;
								break;
							}
					}
				}
				unknownFieldsCodedOutput.flush();
				this.unknownFields = unknownFieldsOutput.toByteString();
				return true;
			} catch (IOException e) {
				return false;
			}
		}

		public void writeToWithCachedSizes(CodedOutputStream output) throws IOException {
			int bytesWrittenBefore = output.getTotalBytesWritten();
			if ((this.bitField0_ & 1) == 1) {
				output.writeMessageWithCachedSizes(2, this.resource_);
			}
			output.writeRawBytes(this.unknownFields);
			if (getCachedSize() != output.getTotalBytesWritten() - bytesWrittenBefore) {
				throw new RuntimeException("Serialized size doesn't match cached size. You may forget to call getSerializedSize() or the message is being modified concurrently.");
			}
		}

		public int getSerializedSize() {
			int size = 0;
			if ((this.bitField0_ & 1) == 1) {
				size = 0 + CodedOutputStream.computeMessageSize(2, this.resource_);
			}
			int size2 = size + this.unknownFields.size();
			this.cachedSize = size2;
			return size2;
		}

		/* access modifiers changed from: protected */
		public Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public OptionalResource clear() {
			assertMutable();
			super.clear();
			if (this.resource_ != Resource.getDefaultInstance()) {
				this.resource_.clear();
			}
			this.bitField0_ &= -2;
			return this;
		}

		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof OptionalResource)) {
				return super.equals(obj);
			}
			OptionalResource other = (OptionalResource) obj;
			boolean result = 1 != 0 && hasResource() == other.hasResource();
			if (hasResource()) {
				result = result && getResource().equals(other.getResource());
			}
			return result;
		}

		public int hashCode() {
			int hash = 41;
			if (hasResource()) {
				int hash2 = 1517 + 2;
				hash = 80507 + getResource().hashCode();
			}
			return (hash * 29) + this.unknownFields.hashCode();
		}

		static {
			defaultInstance.initFields();
			defaultInstance.makeImmutable();
		}

		/* access modifiers changed from: protected */
		public MessageLite internalImmutableDefault() {
			if (immutableDefault == null) {
				immutableDefault = internalImmutableDefault("com.google.analytics.containertag.proto.Serving$OptionalResource");
			}
			return immutableDefault;
		}
	}
}
