import java.io.DataInputStream;
import java.io.IOException;

/**
 * Class 文件读取器
 */
class ClassReader {

	/**
	 * 读取数据流并转换为 ClassFile 实例
	 *
	 * @param is DataInputStream，class 文件的输入流
	 * @return ClassFile 实例
	 */  
	static ClassFile read(DataInputStream is) throws IOException {
		int magic = is.readInt(); // magic number
		int minorVersion = is.readUnsignedShort(); // minor version
		int majorVersion = is.readUnsignedShort(); // major version

		// constant pool
		int cpSize = is.readUnsignedShort(); // constant_pool_count
		CpInfo[] cpInfo = new CpInfo[cpSize]; // constant_pool
		for (int i = 1; i < cpSize; i++) {
			int tag = is.readUnsignedByte();
			byte[] info = null;
			int step = 0;
			switch (tag) {
				case Const.CONSTANT_Fieldref:
				case Const.CONSTANT_Methodref:
				case Const.CONSTANT_InterfaceMethodref:
				case Const.CONSTANT_InvokeDynamic:
				case Const.CONSTANT_NameAndType:
				case Const.CONSTANT_Integer:
				case Const.CONSTANT_Float:
					info = new byte[4];
					is.read(info);
					break;
				case Const.CONSTANT_MethodHandle:
					info = new byte[3];
					is.read(info);
					break;
				case Const.CONSTANT_Class:
				case Const.CONSTANT_String:
				case Const.CONSTANT_MethodType:
					info = new byte[2];
					is.read(info);
					break;
				case Const.CONSTANT_Utf8:
					int ulen = is.readUnsignedShort();
					info = new byte[ulen];
					is.read(info);
					break;
				case Const.CONSTANT_Double:
				case Const.CONSTANT_Long:
					info = new byte[8];
					is.read(info);
					step = 1;
					break;
				default:
					throw new IllegalStateException("unknown tag, " + tag);
			}
			cpInfo[i] = new CpInfo(new U1(tag), info);
			i += step;
		}

		int accessFlags = is.readUnsignedShort();
		int thisClass = is.readUnsignedShort();
		int superClass = is.readUnsignedShort();

		int interfacesCount = is.readUnsignedShort();
		U2[] interfaces = new U2[interfacesCount];
		for (int i = 0; i < interfacesCount; i++) {
			interfaces[i] = new U2(is.readUnsignedShort());
		}

		int fieldsCount = is.readUnsignedShort();
		FieldInfo[] fields = new FieldInfo[fieldsCount];
		for (int i = 0; i < fieldsCount; i++) {
			int fieldAccessFlags = is.readUnsignedShort();
			int fieldNameIndex = is.readUnsignedShort();
			int fieldDescriptor = is.readUnsignedShort();
			int fieldAttributesCount = is.readUnsignedShort();
			AttributeInfo[] fieldAttributeInfo = new AttributeInfo[fieldAttributesCount];
			for (int i1 = 0; i1 < fieldAttributesCount; i1++) {
				int fieldAttributeNameIndex = is.readUnsignedShort();
				int fieldAttributeLength = is.readInt();
				byte[] info = new byte[fieldAttributeLength];
				is.read(info);

				fieldAttributeInfo[i1] = new AttributeInfo(new U2(fieldAttributeNameIndex), new U4(fieldAttributeLength),
						info);
			}

			fields[i] = new FieldInfo(
					new U2(fieldAccessFlags),
					new U2(fieldNameIndex),
					new U2(fieldDescriptor),
					new U2(fieldAttributesCount),
					fieldAttributeInfo);
		}

		int methodsCount = is.readUnsignedShort();
		MethodInfo[] methods = new MethodInfo[methodsCount];
		for (int i = 0; i < methodsCount; i++) {
			int methodAccessFlags = is.readUnsignedShort();
			int methodNameIndex = is.readUnsignedShort();
			int methodDescriptor = is.readUnsignedShort();
			int methodAttributesCount = is.readUnsignedShort();
			AttributeInfo[] methodAttributeInfo = new AttributeInfo[methodAttributesCount];
			for (int j = 0; j < methodAttributesCount; j++) {
				int methodAttributeNameIndex = is.readUnsignedShort();
				int methodAttributeLength = is.readInt();
				byte[] info = new byte[methodAttributeLength];
				is.read(info);

				methodAttributeInfo[j] = new AttributeInfo(
						new U2(methodAttributeNameIndex),
						new U4(methodAttributeLength),
						info);
			}

			methods[i] = new MethodInfo(
					new U2(methodAccessFlags),
					new U2(methodNameIndex),
					new U2(methodDescriptor),
					new U2(methodAttributesCount),
					methodAttributeInfo);
		}

		int attributeCount = is.readUnsignedShort();
		AttributeInfo[] attributeInfos = new AttributeInfo[attributeCount];
		for (int i = 0; i < attributeCount; i++) {
			int attributeNameIndex = is.readUnsignedShort();
			int attributeLength = is.readInt();
			byte[] info = new byte[attributeLength];
			is.read(info);

			attributeInfos[i] = new AttributeInfo(new U2(attributeNameIndex), new U4(attributeLength), info);
		}

		return new ClassFile(
				new U4(magic),
				new U2(minorVersion),
				new U2(majorVersion),
				new U2(cpSize),
				cpInfo,
				new U2(accessFlags),
				new U2(thisClass),
				new U2(superClass),
				new U2(interfacesCount),
				interfaces,
				new U2(fieldsCount),
				fields,
				new U2(methodsCount),
				methods,
				new U2(attributeCount),
				attributeInfos
				);
	}
}