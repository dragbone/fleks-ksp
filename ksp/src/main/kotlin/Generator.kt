import com.squareup.kotlinpoet.*

fun generateSystem(
    className: ClassName,
    requiredFamilies: List<ClassName>,
    optionalFamilies: List<ClassName>,
): FileSpec {
    val systemName = "Base" + className.simpleName
    val familyCodeBlock = CodeBlock.builder()
        .addStatement("""World.family { all(${requiredFamilies.joinToString { it.simpleName }}) }""")
        .build()

    val systemType = TypeSpec.classBuilder(systemName)
        .addModifiers(KModifier.ABSTRACT)
        .superclass(ClassName.bestGuess("com.github.quillraven.fleks.IteratingSystem"))
        .addSuperclassConstructorParameter(familyCodeBlock)
        .addFunction(typeSafeOnTickEntity(requiredFamilies, optionalFamilies))
        .addFunction(callTypeSafeOnTickEntity(requiredFamilies, optionalFamilies))
        .build()

    val file = FileSpec.builder(ClassName(className.packageName, systemName))
        .addImport("com.github.quillraven.fleks", "World")
        .addFileComment("This file is generated, changes will be lost.")
        .addType(systemType)
        .build()

    return file
}

private fun typeSafeOnTickEntity(
    requiredFamilies: List<ClassName>,
    optionalFamilies: List<ClassName>
) = FunSpec.builder("onTickEntity")
    .addModifiers(KModifier.ABSTRACT)
    .addParameter("entity", ClassName.bestGuess("com.github.quillraven.fleks.Entity"))
    .addParameters(requiredFamilies.map {
        ParameterSpec(it.simpleName.variableName, it)
    })
    .addParameters(optionalFamilies.map {
        ParameterSpec(it.simpleName.variableName, it.copy(nullable = true))
    })
    .build()

private fun callTypeSafeOnTickEntity(
    requiredFamilies: List<ClassName>,
    optionalFamilies: List<ClassName>
) = FunSpec.builder("onTickEntity")
    .addModifiers(KModifier.OVERRIDE)
    .addParameter("entity", ClassName.bestGuess("com.github.quillraven.fleks.Entity"))
    .addCode(
        CodeBlock.builder()
            .addStatement("onTickEntity(")
            .indent()
            .addStatement("entity,")
            .apply {
                val components =
                    requiredFamilies.map { "entity[${it.simpleName}]" } + optionalFamilies.map { "entity.getOrNull(${it.simpleName})" }
                components.forEach {
                    addStatement("$it,")
                }
            }
            .unindent()
            .addStatement(")")
            .build()
    )
    .build()

private val String.variableName: String
    get() = replaceFirstChar { it.lowercaseChar() }
