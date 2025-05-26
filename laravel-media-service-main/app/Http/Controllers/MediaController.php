<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Media;
use Illuminate\Support\Facades\Validator;

class MediaController extends Controller
{
   // api conteoller

   // get all media by post id
   public function getMediaByPostId($post_id)
   {
       $media = Media::where('post_id', $post_id)->get();
         if ($media->isEmpty()) {
              return response()->json([], 200);
         }
         return response()->json($media, 200);
   }

    // get all media
    public function getAllMedia()
    {
        $media = Media::all();
        return response()->json($media);
    }
    // get media by id
    public function getMediaById($id)
    {
        $media = Media::find($id);
        if (!$media) {
            return response()->json(['message' => 'Media not found'], 404);
        }
        return response()->json($media, 200);
    }
    // create media upload file and save to database
    public function createMedia(Request $request)
    {
        $validated = $request->validate([
           'url_media' => ['required', 'image','mimes:jpg,jpeg,png,gif,mp4,mov,avi', 'max:4096'],
           'post_id' => ['required', 'numeric'],
        ]);


        $validated['url_media'] = $request->file('url_media')->store('medias', 'public');
        $media = new Media();
        //get name from file
        $media->name = $request->file('url_media')->getClientOriginalName();
        $media->url_media = $validated['url_media'];
        $media->post_id = $request->post_id;
        $media->save();
        return response()->json($media, 201);

    }
    // update media
    public function updateMedia($id, Request $request)
    {
        $media = Media::find($id);
        if (!$media) {
            return response()->json(['message' => 'Media not found'], 404);
        }

        $validated = $request->validate([
            'url_media' => ['required', 'image','mimes:jpg,jpeg,png,gif,mp4,mov,avi', 'max:4096'],
            'post_id' => ['required', 'numeric'],
         ]);


        $validated['url_media'] = $request->file('url_media')->store('media_update', 'public');
        $media->name = $request->file('url_media')->getClientOriginalName();
        $media->url_media = $validated['url_media'];
        $media->post_id = $request->post_id;
        $media->save();

        return response()->json($media, 200);
    }

    // delete media
    public function deleteMedia($id)
    {
        $media = Media::find($id);
        if (!$media) {
            return response()->json(['message' => 'Media not found'], 404);
        }

        $media->delete();

        return response()->json(['message' => 'Media deleted successfully'], 200);
    }
    // delete all media by post id
    public function deleteAllMediaByPostId($post_id)
    {
        $media = Media::where('post_id', $post_id);
        if ($media->count() == 0) {
            return response()->json(['message' => 'No media found for this post'], 404);
        }
        $media->delete();
        return response()->json(['message' => 'All media deleted successfully'], 200);
    }
}
