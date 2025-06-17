package datos.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import modelo.Receta;

public class RecetaTypeAdapter extends TypeAdapter<Receta> {
	private Gson gson;
	
    public RecetaTypeAdapter() {
    }
    
	public void setGson(Gson gson) {
		this.gson = gson;
	}

	@Override
	public void write(JsonWriter out, Receta value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        // Convert domain Receta to RecetaSerializable and write it
        RecetaSerializable serializableReceta = RecetaSerializable.fromReceta(value);
        // Use the shared gson instance for writing the serializable object
        gson.toJson(serializableReceta, RecetaSerializable.class, out);
    }

	@Override
    public Receta read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        // Read into RecetaSerializable, then convert to domain Receta
        RecetaSerializable serializableReceta = gson.fromJson(in, RecetaSerializable.class);
        if (serializableReceta == null) { // Handle potential null deserialization
            return null;
        }
        try {
            return serializableReceta.toReceta();
        } catch (IllegalStateException e) {
            // Catch the IllegalStateException from toReceta() (e.g., if produced object is basic)
            // and re-throw as JsonParseException for GSON to handle
            throw new JsonParseException("Error deserializing Receta: " + e.getMessage(), e);
        }
    }
}
