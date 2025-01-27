package com.example

import org.gradle.api.Action
import org.gradle.api.artifacts.ComponentMetadataContext
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.artifacts.VariantMetadata
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import org.gradle.api.attributes.java.TargetJvmEnvironment
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named
import javax.inject.Inject

/**
 * This one works
 * we need to create a new variant compilePlusRuntime and give it an extra variant so that there is no ambiguity error
 *
 */
abstract class ViewAttributesRule @Inject internal constructor(objectFactory: ObjectFactory) : ComponentMetadataRule {

    private val jarLibraryElements: LibraryElements
    private val libraryCategory: Category
    private val javaRuntimeUsage: Usage
    private val javaApiUsage: Usage
    private val optimizedJvm: TargetJvmEnvironment

    init {
        jarLibraryElements = objectFactory.named(LibraryElements.JAR)
        libraryCategory = objectFactory.named(Category.LIBRARY)
        javaRuntimeUsage = objectFactory.named(Usage.JAVA_RUNTIME)
        javaApiUsage = objectFactory.named(Usage.JAVA_API)
        optimizedJvm = objectFactory.named(TargetJvmEnvironment.STANDARD_JVM)

    }

    override fun execute(context: ComponentMetadataContext) {



        // Add a new variant with an extra attribute
        context.details.maybeAddVariant("compilePlusRuntime", "runtimeElements") {
            attributes {
                attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, jarLibraryElements)
                attribute(Category.CATEGORY_ATTRIBUTE, libraryCategory)
                attribute(Usage.USAGE_ATTRIBUTE, javaApiUsage)
                // Add this extra attribute
                attribute(TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE, optimizedJvm)
            }
        }

        // Compare attributes of apiElements and the new variant
        context.details.withVariant("apiElements", object : Action<VariantMetadata> {
            override fun execute(variant: VariantMetadata) {
                variant.attributes(object : Action<AttributeContainer> {
                    override fun execute(attributes: AttributeContainer) {
                        println("########### apiElements attributes")
                        println(attributes)
                        println("###########")
                    }

                })
            }
        })
        context.details.withVariant("compilePlusRuntime", object : Action<VariantMetadata> {
            override fun execute(variant: VariantMetadata) {
                variant.attributes(object : Action<AttributeContainer> {
                    override fun execute(attributes: AttributeContainer) {
                        println("########### compilePlusRuntime attributes")
                        println(attributes)
                        println("###########")
                    }
                })
            }
        })
    }
}