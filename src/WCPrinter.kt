import java.io.BufferedOutputStream
import java.io.DataOutputStream
import java.io.FileOutputStream

class WCPrinter {
    companion object {
        fun print(path: String, instrs: List<UInt>) {
            val dos = DataOutputStream(BufferedOutputStream(FileOutputStream("tmp/$path.wc")))
            dos.writeBytes("RCWT") // magic
            dos.writeShort(4 * (instrs.size + 1)) // text_size
            dos.writeShort(0) // data_size
            dos.writeShort(1) // defined_count
            dos.writeShort(0) // undefined_count
            dos.writeShort(0) // relocation_count
            dos.writeBytes("main")
            dos.writeByte(0) // null-byte
            dos.writeShort(0) // segment_id
            dos.writeShort(0) // base_address
            instrs.map{x -> x.toInt()}.forEach(dos::writeInt)
            dos.writeInt(0x41000000) // EXIT
            dos.flush()
            dos.close()
        }
    }
}