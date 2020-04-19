import option.App
import option.Arg
import java.io.File
import java.nio.file.Paths

fun main(args : Array<String>) {
    val app = App("incremental")
            .version("0.3.0")
            .author("skyrabbit <iamskyrabbit@gmail.com>")
            .description("a toy language processor")
            .arg(Arg("path")
                    .description("path of file to compile")
                    .short("")
                    .takesValue(true))
            .arg(Arg("debug")
                    .description("show debug information")
                    .short("d"))
    val matches = app.matches(args)
    val path = matches.getValue("path") ?: return
    File(Paths.get("tmp/$path").parent.toString()).mkdirs()
    if (matches.contains("debug")) Parser.parseD(path)
    else Parser.parse(path)
}