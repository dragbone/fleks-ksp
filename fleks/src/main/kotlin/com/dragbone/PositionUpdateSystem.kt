package com.dragbone

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

@EKSystem(
    required = [Position::class, Velocity::class]
)
class PositionUpdateSystem : BasePositionUpdateSystem() {
    override fun onTickEntity(entity: Entity, position: Position, velocity: Velocity) {
        position.x += velocity.x * deltaTime
        position.y += velocity.y * deltaTime
        position.z += velocity.z * deltaTime
    }
}

data class Position(
    var x: Float,
    var y: Float,
    var z: Float,
) : Component<Position> {
    override fun type(): ComponentType<Position> = Position

    companion object : ComponentType<Position>()
}

data class Velocity(
    var x: Float,
    var y: Float,
    var z: Float,
) : Component<Velocity> {
    override fun type(): ComponentType<Velocity> = Velocity

    companion object : ComponentType<Velocity>()
}
