package temp

data class User(
    val userId: Long,
    val nombre: String,
    val edad: Int,
    val domicilio_calle: String,
    val domicilio_no_exterior: String,
    val domicilio_no_interior: String,
    val domicilio_colonia: String,
    )

fun main(args: Array<String>)
{
    var name = ""
    User::class.java.declaredFields.forEach {
        name = it.name
        println(
        """
        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/${name}"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:hint="@string/${name}"
            >

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:minLines="2"
                android:text="@{User.${name}}"
                />
        </com.google.android.material.textfield.TextInputLayout>
        
        """.trimIndent()
        )
    }
}