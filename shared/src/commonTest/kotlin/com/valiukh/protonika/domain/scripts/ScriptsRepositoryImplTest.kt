package com.valiukh.protonika.domain.scripts

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ScriptsRepositoryImplTest {

    @Test
    fun `addScript then getScript returns the stored script`() {
        val repository = ScriptsRepositoryImpl()

        repository.addScript(name = "factorial.mk61", script = "15\nX→П 8")

        assertEquals("15\nX→П 8", repository.getScript("factorial.mk61"))
    }

    @Test
    fun `addScript overwrites an existing script with the same name`() {
        val repository = ScriptsRepositoryImpl()

        repository.addScript(name = "script.mk61", script = "first")
        repository.addScript(name = "script.mk61", script = "second")

        assertEquals("second", repository.getScript("script.mk61"))
        assertEquals(listOf("script.mk61"), repository.getAllScriptsNames())
    }

    @Test
    fun `getScript throws when the script is missing`() {
        val repository = ScriptsRepositoryImpl()

        assertFailsWith<IllegalArgumentException> {
            repository.getScript("missing.mk61")
        }
    }

    @Test
    fun `getAllScriptsNames returns every stored name`() {
        val repository = ScriptsRepositoryImpl()

        repository.addScript(name = "a.mk61", script = "a")
        repository.addScript(name = "b.mk61", script = "b")

        val names = repository.getAllScriptsNames()

        assertEquals(2, names.size)
        assertTrue(names.containsAll(listOf("a.mk61", "b.mk61")))
    }

    @Test
    fun `getAllScriptsNames is empty for a new repository`() {
        val repository = ScriptsRepositoryImpl()

        assertTrue(repository.getAllScriptsNames().isEmpty())
    }

    @Test
    fun `getAllScripts returns a snapshot that is unaffected by later writes`() {
        val repository = ScriptsRepositoryImpl()
        repository.addScript(name = "a.mk61", script = "a")

        val snapshot = repository.getAllScripts()
        repository.addScript(name = "b.mk61", script = "b")

        assertEquals(mapOf("a.mk61" to "a"), snapshot)
    }
}
