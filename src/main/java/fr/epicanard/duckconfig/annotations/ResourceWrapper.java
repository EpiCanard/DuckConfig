package fr.epicanard.duckconfig.annotations;


public class ResourceWrapper {
  final public String value;
  final public ResourceType type;
  final public ResourceLocation location;
  final public String basePath;

  public ResourceWrapper(final String basePath, final String value, final ResourceLocation location, final ResourceType type) {
    this.basePath = basePath;
    this.value = value;
    this.location = location;
    this.type = type;
  }

  public ResourceWrapper(final String basePath, final String value, final ResourceLocation location) {
    this(basePath, value, location, ResourceType.YAML);
  }

  public ResourceWrapper(final String basePath, final String value) {
    this(basePath, value, ResourceLocation.FILE_PATH, ResourceType.YAML);
  }

  public ResourceWrapper(final String basePath, final Resource resource) {
    this(basePath, resource.value(), resource.location(), resource.type());
  }

}