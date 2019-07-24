package uk.co.grahamcox.goworlds.service.worlds.http

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uk.co.grahamcox.goworlds.service.http.buildUri
import uk.co.grahamcox.goworlds.service.http.problem.ProblemModel
import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.worlds.UnknownWorldException
import uk.co.grahamcox.goworlds.service.worlds.WorldData
import uk.co.grahamcox.goworlds.service.worlds.WorldId
import uk.co.grahamcox.goworlds.service.worlds.WorldService
import java.net.URI
import java.util.*

/**
 * Controller for interacting with Worlds
 */
@RestController
@RequestMapping("/worlds")
class WorldsController(private val worldService: WorldService) {

    /**
     * Get the World with the given ID
     * @param id The ID of the world
     * @return the world
     */
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    fun getWorldById(@PathVariable("id") id: String) : ResponseEntity<WorldModel> {
        val worldId = try {
            WorldId(UUID.fromString(id))
        } catch (e: IllegalArgumentException) {
            throw UnknownWorldException(null)
        }
        val world = worldService.getById(worldId)

        return buildWorldResponse(world)
    }

    /**
     * Get the World with the given slug
     * @param slug The slug of the world
     * @return the world
     */
    @RequestMapping(value = ["/by-slug/{slug}"], method = [RequestMethod.GET])
    fun getWorldBySlug(@PathVariable("slug") slug: String) : ResponseEntity<WorldModel> {
        val world = worldService.getBySlug(slug)

        return buildWorldResponse(world)
    }

    /**
     * Build the World Model response that represents the returned World
     * @param world The world to translate
     * @return the translated World Model
     */
    private fun buildWorldModel(world: Model<WorldId, WorldData>): WorldModel {
        return WorldModel(
                id = world.identity.id.id.toString(),
                created = world.identity.created,
                updated = world.identity.updated,
                name = world.data.name,
                description = world.data.description,
                slug = world.data.slug,
                owner = world.data.owner.id.toString()
        )
    }

    /**
     * Build an HTTP Response for a single world
     * @param world The world
     * @return the response
     */
    private fun buildWorldResponse(world: Model<WorldId, WorldData>) : ResponseEntity<WorldModel> {
        val worldModel = buildWorldModel(world)
        return ResponseEntity.ok()
                .eTag("\"" + world.identity.version + "\"")
                .lastModified(world.identity.updated)
                .header(HttpHeaders.CONTENT_LOCATION, WorldsController::getWorldById.buildUri(world.identity.id.id.toString()).toString())
                .header("Accept-Patch", "application/merge-patch+json")
                .body(worldModel)
    }

    /**
     * Handler for when the requested world can't be found
     */
    @ExceptionHandler(UnknownWorldException::class)
    fun handleUnknownWorld() = ProblemModel(
            type = URI("tag:goworlds,2019:worlds/problems/unknown-world"),
            title = "The requested world could not be found",
            statusCode = HttpStatus.NOT_FOUND
    )
}