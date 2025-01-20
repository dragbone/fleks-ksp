import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

class EKSystemProcessor(
    private val codeGenerator: CodeGenerator,
    private val options: Map<String, String>
) : SymbolProcessor {
    private var hasRun = false
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (hasRun) return emptyList()
        hasRun = true

        val logFile = codeGenerator.createNewFile(Dependencies(false), "", "EKSystemProcessor", "log")
        logFile.bufferedWriter().use { log ->
            fun log(message: Any) = log.write("$message\n")

            log("Running EKSystemProcessor processor...")

            resolver.getSymbolsWithAnnotation("com.dragbone.EKSystem").forEach { symbol ->
                val annotation = symbol.annotations.single { it.shortName.asString() == "EKSystem" }
                val components = annotation.arguments.map {
                    @Suppress("UNCHECKED_CAST")
                    val components = it.value as ArrayList<KSType>
                    components.toList().map { it.toClassName() }
                }
                (symbol as? KSClassDeclaration)?.let { clazz ->
                    val name = "Base${clazz.simpleName.asString()}"
                    log("Generating $name:")
                    log("   required: ${components[0].joinToString { it.simpleName }}")
                    log("   optional: ${components[1].joinToString { it.simpleName }}")

                    val fileSpec = generateSystem(clazz.toClassName(), components[0], components[1])
                    fileSpec.writeTo(codeGenerator, Dependencies(false, clazz.containingFile!!))
                }
            }
        }
        return emptyList()
    }
}

class EKSystemProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return EKSystemProcessor(environment.codeGenerator, environment.options)
    }
}
