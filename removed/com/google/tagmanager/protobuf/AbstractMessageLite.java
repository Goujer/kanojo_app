// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
// https://developers.google.com/protocol-buffers/
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
//     * Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above
// copyright notice, this list of conditions and the following disclaimer
// in the documentation and/or other materials provided with the
// distribution.
//     * Neither the name of Google Inc. nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.ByteString;
import com.google.tagmanager.protobuf.MessageLite;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

public abstract class AbstractMessageLite implements MessageLite {
	protected int memoizedHashCode = 0;

	public ByteString toByteString() {
		try {
			ByteString.CodedBuilder out = ByteString.newCodedBuilder(getSerializedSize());
			writeTo(out.getCodedOutput());
			return out.build();
		} catch (IOException e) {
			throw new RuntimeException("Serializing to a ByteString threw an IOException (should never happen).", e);
		}
	}

	public byte[] toByteArray() {
		try {
			byte[] result = new byte[getSerializedSize()];
			CodedOutputStream output = CodedOutputStream.newInstance(result);
			writeTo(output);
			output.checkNoSpaceLeft();
			return result;
		} catch (IOException e) {
			throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
		}
	}

	public void writeTo(OutputStream output) throws IOException {
		CodedOutputStream codedOutput = CodedOutputStream.newInstance(output, CodedOutputStream.computePreferredBufferSize(getSerializedSize()));
		writeTo(codedOutput);
		codedOutput.flush();
	}

	public void writeDelimitedTo(OutputStream output) throws IOException {
		int serialized = getSerializedSize();
		CodedOutputStream codedOutput = CodedOutputStream.newInstance(output, CodedOutputStream.computePreferredBufferSize(CodedOutputStream.computeRawVarint32Size(serialized) + serialized));
		codedOutput.writeRawVarint32(serialized);
		writeTo(codedOutput);
		codedOutput.flush();
	}

	public MutableMessageLite mutableCopy() {
		throw new UnsupportedOperationException("mutableCopy() is not implemented.");
	}

	/* access modifiers changed from: package-private */
	public UninitializedMessageException newUninitializedMessageException() {
		return new UninitializedMessageException((MessageLite) this);
	}

	protected static void checkByteStringIsUtf8(ByteString byteString) throws IllegalArgumentException {
		if (!byteString.isValidUtf8()) {
			throw new IllegalArgumentException("Byte string is not UTF-8.");
		}
	}

	public static abstract class Builder<BuilderType extends Builder> implements MessageLite.Builder {
		public abstract BuilderType clone();

		public abstract BuilderType mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException;

		public BuilderType mergeFrom(CodedInputStream input) throws IOException {
			return mergeFrom(input, ExtensionRegistryLite.getEmptyRegistry());
		}

		@Override
		public BuilderType mergeFrom(final ByteString data) throws InvalidProtocolBufferException {
			try {
				final CodedInputStream input = data.newCodedInput();
				mergeFrom(input);
				input.checkLastTagWas(0);
				return (BuilderType) this;
			} catch (InvalidProtocolBufferException e) {
				throw e;
			} catch (IOException e) {
				throw new RuntimeException(getReadingExceptionMessage("ByteString"), e);
			}
		}

		@Override
		public BuilderType mergeFrom(
				final ByteString data, final ExtensionRegistryLite extensionRegistry)
				throws InvalidProtocolBufferException {
			try {
				final CodedInputStream input = data.newCodedInput();
				mergeFrom(input, extensionRegistry);
				input.checkLastTagWas(0);
				return (BuilderType) this;
			} catch (InvalidProtocolBufferException e) {
				throw e;
			} catch (IOException e) {
				throw new RuntimeException(getReadingExceptionMessage("ByteString"), e);
			}
		}

		@Override
		public BuilderType mergeFrom(final byte[] data) throws InvalidProtocolBufferException {
			return mergeFrom(data, 0, data.length);
		}

		@Override
		public BuilderType mergeFrom(final byte[] data, final int off, final int len)
				throws InvalidProtocolBufferException {
			try {
				final CodedInputStream input = CodedInputStream.newInstance(data, off, len);
				mergeFrom(input);
				input.checkLastTagWas(0);
				return (BuilderType) this;
			} catch (InvalidProtocolBufferException e) {
				throw e;
			} catch (IOException e) {
				throw new RuntimeException(getReadingExceptionMessage("byte array"), e);
			}
		}

		@Override
		public BuilderType mergeFrom(final byte[] data, final ExtensionRegistryLite extensionRegistry)
				throws InvalidProtocolBufferException {
			return mergeFrom(data, 0, data.length, extensionRegistry);
		}

		@Override
		public BuilderType mergeFrom(
				final byte[] data,
				final int off,
				final int len,
				final ExtensionRegistryLite extensionRegistry)
				throws InvalidProtocolBufferException {
			try {
				final CodedInputStream input = CodedInputStream.newInstance(data, off, len);
				mergeFrom(input, extensionRegistry);
				input.checkLastTagWas(0);
				return (BuilderType) this;
			} catch (InvalidProtocolBufferException e) {
				throw e;
			} catch (IOException e) {
				throw new RuntimeException(getReadingExceptionMessage("byte array"), e);
			}
		}

		@Override
		public BuilderType mergeFrom(final InputStream input) throws IOException {
			final CodedInputStream codedInput = CodedInputStream.newInstance(input);
			mergeFrom(codedInput);
			codedInput.checkLastTagWas(0);
			return (BuilderType) this;
		}

		@Override
		public BuilderType mergeFrom(
				final InputStream input, final ExtensionRegistryLite extensionRegistry) throws IOException {
			final CodedInputStream codedInput = CodedInputStream.newInstance(input);
			mergeFrom(codedInput, extensionRegistry);
			codedInput.checkLastTagWas(0);
			return (BuilderType) this;
		}

		static final class LimitedInputStream extends FilterInputStream {
			private int limit;

			LimitedInputStream(InputStream in, int limit2) {
				super(in);
				this.limit = limit2;
			}

			public int available() throws IOException {
				return Math.min(super.available(), this.limit);
			}

			public int read() throws IOException {
				if (this.limit <= 0) {
					return -1;
				}
				int result = super.read();
				if (result < 0) {
					return result;
				}
				this.limit--;
				return result;
			}

			public int read(byte[] b, int off, int len) throws IOException {
				if (this.limit <= 0) {
					return -1;
				}
				int result = super.read(b, off, Math.min(len, this.limit));
				if (result < 0) {
					return result;
				}
				this.limit -= result;
				return result;
			}

			public long skip(long n) throws IOException {
				long result = super.skip(Math.min(n, (long) this.limit));
				if (result >= 0) {
					this.limit = (int) (((long) this.limit) - result);
				}
				return result;
			}
		}

		public boolean mergeDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			int firstByte = input.read();
			if (firstByte == -1) {
				return false;
			}
			mergeFrom(new LimitedInputStream(input, CodedInputStream.readRawVarint32(firstByte, input)), extensionRegistry);
			return true;
		}

		public boolean mergeDelimitedFrom(InputStream input) throws IOException {
			return mergeDelimitedFrom(input, ExtensionRegistryLite.getEmptyRegistry());
		}

		private String getReadingExceptionMessage(String target) {
			return "Reading "
					+ getClass().getName()
					+ " from a "
					+ target
					+ " threw an IOException (should never happen).";
		}

		protected static UninitializedMessageException newUninitializedMessageException(MessageLite message) {
			return new UninitializedMessageException(message);
		}

		protected static <T> void addAll(Iterable<T> values, Collection<? super T> list) {
			if (values instanceof LazyStringList) {
				checkForNullValues(((LazyStringList) values).getUnderlyingElements());
				list.addAll((Collection) values);
			} else if (values instanceof Collection) {
				checkForNullValues(values);
				list.addAll((Collection) values);
			} else {
				for (T value : values) {
					if (value == null) {
						throw new NullPointerException();
					}
					list.add(value);
				}
			}
		}

		private static void checkForNullValues(Iterable<?> values) {
			for (Object value : values) {
				if (value == null) {
					throw new NullPointerException();
				}
			}
		}
	}
}
